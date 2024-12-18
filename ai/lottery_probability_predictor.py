
import pandas as pd
import numpy as np
from collections import Counter, defaultdict
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, LSTM, Dropout, BatchNormalization
import json

class ProbabilityLotteryPredictor:
    def __init__(self, historical_weight=0., pattern_weight=0.3):
        self.historical_weight = historical_weight
        self.pattern_weight = pattern_weight
        self.number_probabilities = None
        self.pattern_matrix = None
        self.lstm_model = None
        self.scaler = MinMaxScaler()
        
    def calculate_historical_probabilities(self, numbers):
        """Calculate probability distribution based on historical frequency"""
        # Flatten all numbers and count frequencies
        all_numbers = numbers.flatten()
        number_counts = Counter(all_numbers)
        
        # Calculate basic probabilities based on frequency
        total_draws = len(all_numbers)
        probabilities = {num: count/total_draws for num, count in number_counts.items()}
        
        # Ensure all numbers 1-45 have a probability
        for num in range(1, 46):
            if num not in probabilities:
                probabilities[num] = 1/total_draws
                
        return probabilities

    def analyze_number_patterns(self, numbers):
        """Analyze patterns in number combinations"""
        pattern_matrix = np.zeros((45, 45))
        
        # Count co-occurrences of numbers
        for draw in numbers:
            for i in draw:
                for j in draw:
                    if i != j:
                        pattern_matrix[i-1][j-1] += 1
                        
        # Normalize pattern matrix
        pattern_matrix = pattern_matrix / len(numbers)
        return pattern_matrix

    def calculate_number_ranges(self, numbers):
        """Calculate probabilities for different number ranges"""
        ranges = {
            'low': (1, 15),
            'medium': (16, 30),
            'high': (31, 45)
        }
        
        range_counts = defaultdict(int)
        total_numbers = len(numbers) * 6
        
        for draw in numbers:
            for num in draw:
                for range_name, (start, end) in ranges.items():
                    if start <= num <= end:
                        range_counts[range_name] += 1
                        
        return {range_name: count/total_numbers for range_name, count in range_counts.items()}

    def build_lstm_model(self, input_shape):
        """Build LSTM model for sequence prediction"""
        model = Sequential([
            LSTM(64, input_shape=input_shape, return_sequences=True),
            BatchNormalization(),
            Dropout(0.3),
            
            LSTM(32),
            BatchNormalization(),
            Dropout(0.3),
            
            Dense(45, activation='softmax')  # Probability distribution over all numbers
        ])
        
        model.compile(optimizer='adam', loss='categorical_crossentropy')
        return model

    def prepare_sequence_data(self, numbers, seq_length=5):
        """Prepare sequence data for LSTM"""
        X, y = [], []
        
        for i in range(len(numbers) - seq_length):
            seq = numbers[i:i+seq_length]
            target = numbers[i+seq_length]
            
            # Convert target to one-hot encoding
            target_onehot = np.zeros(45)
            for num in target:
                target_onehot[num-1] = 1
                
            X.append(seq)
            y.append(target_onehot)
            
        return np.array(X), np.array(y)

    def train(self, numbers, epochs=100):
        """Train the complete prediction system"""
        # Calculate historical probabilities
        self.number_probabilities = self.calculate_historical_probabilities(numbers)
        
        # Analyze patterns
        self.pattern_matrix = self.analyze_number_patterns(numbers)
        
        # Train LSTM model
        X, y = self.prepare_sequence_data(numbers)
        self.lstm_model = self.build_lstm_model((X.shape[1], X.shape[2]))
        self.lstm_model.fit(X, y, epochs=epochs, batch_size=32, verbose=0)

    def get_combined_probabilities(self, recent_numbers):
        """Combine different probability sources"""
        # Historical probabilities
        hist_probs = np.array([self.number_probabilities[i] for i in range(1, 46)])
        
        # Pattern-based probabilities
        pattern_probs = np.mean(self.pattern_matrix[recent_numbers[-1]-1], axis=0)
        
        # LSTM predictions
        seq_data = recent_numbers[-5:].reshape(1, 5, 6)
        lstm_probs = self.lstm_model.predict(seq_data)[0]
        
        # Combine probabilities
        combined_probs = (
            self.historical_weight * hist_probs +
            self.pattern_weight * pattern_probs +
            (1 - self.historical_weight - self.pattern_weight) * lstm_probs
        )
        
        return combined_probs

    def predict_next_numbers(self, recent_numbers, num_simulations=1000, top_k=5):
        """Predict next lottery numbers using combined probability approach"""
        combined_probs = self.get_combined_probabilities(recent_numbers)
        
        # Run multiple simulations
        all_predictions = []
        for _ in range(num_simulations):
            # Sample numbers based on probabilities
            prediction = []
            temp_probs = combined_probs.copy()
            
            while len(prediction) < 6:
                # Normalize remaining probabilities
                temp_probs = temp_probs / np.sum(temp_probs)
                
                # Sample next number
                next_num = np.random.choice(45, p=temp_probs) + 1
                
                if next_num not in prediction:
                    prediction.append(next_num)
                    # Set probability to 0 to avoid reselection
                    temp_probs[next_num-1] = 0
                    
            all_predictions.append(sorted(prediction))
            
        # Find top k most common predictions
        prediction_counts = Counter(tuple(pred) for pred in all_predictions)
        top_predictions = prediction_counts.most_common(top_k)
        
        # Calculate detailed statistics for each top prediction
        results = []
        for pred_tuple, frequency in top_predictions:
            pred_list = list(pred_tuple)
            results.append({
                'numbers': pred_list,
                'frequency': frequency / num_simulations,  # Convert to probability
                'confidence_scores': {num: combined_probs[num-1] for num in pred_list},
                'range_distribution': self.calculate_number_ranges(np.array([pred_list]))
            })
        
        return results

def main():
    # Load data
    df = pd.read_csv('/app/ai/oh-lottery_main.csv')
    winning_numbers = df[['Number 1', 'Number 2', 'Number 3', 
                         'Number 4', 'Number 5', 'Number 6']].values
    
    # Initialize and train predictor
    predictor = ProbabilityLotteryPredictor()
    predictor.train(winning_numbers)
    
    # Make prediction
    recent_numbers = winning_numbers[-5:]
    prediction_results = predictor.predict_next_numbers(recent_numbers, top_k=5)
    
    return json.dumps(prediction_results)

if __name__ == "__main__":
    results = main()
    print(results)