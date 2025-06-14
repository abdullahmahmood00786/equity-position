import React from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { IconButton, Tooltip, Box } from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import TransactionActions from './TransactionAction';






export default function TransactionGrid({ initialTransactions, initialLoading, mainRefresh }) {

  const columns = [
    { field: 'transactionId', headerName: 'ID', width: 150 },
    { field: 'tradeId', headerName: 'Trade ID', width: 150 },
    { field: 'version', headerName: 'Version', width: 140 },
    { field: 'securityCode', headerName: 'Security', width: 160 },
    { field: 'quantity', headerName: 'Quantity', width: 150 },
    { field: 'tradeType', headerName: 'Trade Type', width: 150 },
    { field: 'action', headerName: 'Current Action', width: 150 },
    {
      field: '',
      headerName: '',
      width: 100,
      sortable: false,
      filterable: false,
      disableColumnMenu: true,
      renderCell: (params) => (
        <TransactionActions 
          transaction={params.row} 
          refreshData={mainRefresh}
        />
      )
    }
  ];

  return (
    <Box sx={{ height: 600, width: '100%' }}>
      <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: 1 }}>
        <Tooltip title="Refresh">
          <IconButton onClick={mainRefresh}>
            <RefreshIcon />
          </IconButton>
        </Tooltip>
      </Box>
      
      <DataGrid
        rows={initialTransactions}
        getRowId={(row) => row.transactionId}
        columns={columns}
        pageSize={10}
        rowsPerPageOptions={[10]}
        disableSelectionOnClick
        loading={initialLoading}
      />
    </Box>
  );
}