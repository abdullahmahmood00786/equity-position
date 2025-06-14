import React, { useState, useEffect } from 'react';
import { Container, AppBar, Toolbar, Typography, Tabs, Tab, Box } from '@mui/material';
import { api } from './services/api';
import TransactionGrid from './components/TransactionGrid';
import CreateTransactionForm from './components/CreateTransactionForm';
import PositionsView from './components/PositionsView';

function App() {
  const [tabValue, setTabValue] = useState(0);
  const [transactions, setTransactions] = useState([]);
  const [positions, setPositions] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchData = async () => {
    try {
      const [transactionsRes, positionsRes] = await Promise.all([
        api.getTransactions(),
        api.getPositions()
      ]);
      setTransactions(transactionsRes.data);
      setPositions(positionsRes.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleTabChange = (event, newValue) => {
    setTabValue(newValue);
  };

  const handleTransactionCreated = (newTransaction) => {
    setTransactions([...transactions, newTransaction]);
    fetchData(); // Refresh positions
  };

  return (
    <div className="App">
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6">Equity Positions Manager</Typography>
        </Toolbar>
      </AppBar>
      
      <Container maxWidth="lg" sx={{ mt: 4 }}>
        <Tabs value={tabValue} onChange={handleTabChange}>
          <Tab label="Transactions" />
          <Tab label="Create Transaction" />
          <Tab label="Positions" />
        </Tabs>
        
        <Box sx={{ pt: 3 }}>
          {tabValue === 0 && (
            <TransactionGrid 
            initialTransactions={transactions} 
            initialLoading={loading} 
            mainRefresh={fetchData}
            />
          )}
          
          {tabValue === 1 && (
            <CreateTransactionForm 
              onTransactionCreated={handleTransactionCreated} 
            />
          )}
          
          {tabValue === 2 && (
            <PositionsView 
              positions={positions} 
              loading={loading} 
            />
          )}
        </Box>
      </Container>
    </div>
  );
}

export default App;