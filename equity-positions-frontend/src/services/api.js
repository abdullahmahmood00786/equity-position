import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export const api = {
  // Transaction endpoints
  getTransactions: () => axios.get(`${API_BASE_URL}/transactions`),
  createTransaction: (transaction) => axios.post(`${API_BASE_URL}/transactions`, transaction),
  
  // Position endpoints
  getPositions: () => axios.get(`${API_BASE_URL}/transactions/positions`)
};