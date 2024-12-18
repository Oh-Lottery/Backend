import pandas as pd
import numpy as np
from sklearn.cluster import KMeans
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import mean_squared_error, r2_score
from xgboost import XGBRegressor
import json

# Feature Engineering 함수
def prepare_prediction_features(df):
    # Replace zero winners with NaN
    for col in ['Number of 1st Prize Winner', 'Number of 2nd Prize Winner', 'Number of 3rd Prize Winner']:
        df[col] = df[col].replace(0, np.nan)

    # Derived features for prizes
    for prize, winner in [('1st', 'Number of 1st Prize Winner'), 
                          ('2nd', 'Number of 2nd Prize Winner'),
                          ('3rd', 'Number of 3rd Prize Winner')]:
        df[f'prize_per_{prize}_winner'] = df[f'{prize} Prize Amount'] / (df[winner] + 1)
        df[f'log_{prize}_prize'] = np.log1p(df[f'{prize} Prize Amount'])
        df[f'prev_{prize}_prize'] = df[f'{prize} Prize Amount'].shift(1)

    # Rolling statistics
    for window in [3, 5]:
        df[f'rolling_{window}_prize_mean'] = df['1st Prize Amount'].rolling(window).mean()
        df[f'rolling_{window}_prize_std'] = df['1st Prize Amount'].rolling(window).std()

    df['is_carryover'] = (df['Number of 1st Prize Winner'].shift(1) == 0).astype(int)
    df['month'] = pd.to_datetime(df['Draw Date']).dt.month
    df['weekday'] = pd.to_datetime(df['Draw Date']).dt.weekday

    return df.dropna()

# 이상치 처리 함수
def handle_outliers(df, prize_col, num_winner_col):
    df = df.copy()  # 원본 데이터를 명확히 복사
    
    # 대상 컬럼을 float로 변환
    df.loc[:, prize_col] = df[prize_col].astype(float)
    df.loc[:, num_winner_col] = df[num_winner_col].astype(float)
    
    # 필터링된 복사본 생성
    df_filtered = df[(df[prize_col] > 0) & (df[num_winner_col] > 0)].copy()
    
    # KMeans 군집화 적용
    kmeans = KMeans(n_clusters=3, random_state=42)
    df_filtered.loc[:, 'cluster'] = kmeans.fit_predict(df_filtered[[prize_col, num_winner_col]])
    
    # 군집별 평균값으로 이상치 대체
    for cluster in range(3):
        cluster_mean = df_filtered.loc[df_filtered['cluster'] == cluster, prize_col].mean()
        df.loc[(df[num_winner_col] == 0) | (df[prize_col] == 0), prize_col] = float(cluster_mean)
    
    # cluster 열 제거
    df = df.drop(columns='cluster', errors='ignore')
    return df

# 모델 학습 및 평가 함수 (모델 저장 부분 삭제)
def build_and_evaluate_model(df, target_col):
    df = prepare_prediction_features(df)

    # Feature selection
    features = ['prev_1st_prize', 'rolling_3_prize_mean', 'rolling_5_prize_mean',
                'rolling_3_prize_std', 'rolling_5_prize_std', 
                f'log_{target_col.split()[0]}_prize', 'is_carryover', 'month', 'weekday']

    X = df[features]
    y = df[f'log_{target_col.split()[0]}_prize']

    # Train-test split
    X_train, X_test, y_train, y_test = train_test_split(X.tail(1147), y.tail(1147), test_size=0.2, random_state=42)

    # Scaling
    scaler = MinMaxScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)

    # XGBoost 모델 학습
    model = XGBRegressor(n_estimators=100, max_depth=4, learning_rate=0.03, random_state=42)
    model.fit(X_train_scaled, y_train)

    # 평가
    y_pred = model.predict(X_test_scaled)
    r2 = r2_score(np.expm1(y_test), np.expm1(y_pred))
    mse = mean_squared_error(np.expm1(y_test), np.expm1(y_pred))
    print(f"{target_col} R² Score: {r2:.2f}, MSE: {mse:.2f}")

    # 다음 회차 예측
    latest_data = df.iloc[-1:][features]
    latest_scaled_features = scaler.transform(latest_data)
    log_prediction = model.predict(latest_scaled_features)
    return np.expm1(log_prediction[0])

# 메인 코드
df = pd.read_csv('./oh-lottery_main.csv')

# 1등, 2등, 3등 예측
# features_1st = ['prev_1st_prize', 'rolling_3_prize_mean', 'rolling_5_prize_mean',
#                'rolling_3_prize_std', 'rolling_5_prize_std',
#                'log_1st_prize', 'is_carryover', 'month', 'weekday']

predicted_1st = build_and_evaluate_model(df, '1st Prize Amount')
predicted_2nd = build_and_evaluate_model(df, '2nd Prize Amount')
predicted_3rd = build_and_evaluate_model(df, '3rd Prize Amount')

# 결과 출력
result = {
    "Predicted 1st Prize Amount": float(predicted_1st),
    "Predicted 2nd Prize Amount": float(predicted_2nd),
    "Predicted 3rd Prize Amount": float(predicted_3rd)
}

# JSON 형식으로 출력
print("\n===== Predicted Prize Amounts for Next Round =====")
print(json.dumps(result, indent=4))
