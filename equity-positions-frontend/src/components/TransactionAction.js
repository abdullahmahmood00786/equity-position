import React, { useState } from 'react';
import { 
  IconButton, 
  Menu, 
  MenuItem, 
  Dialog, 
  DialogTitle, 
  DialogContent, 
  DialogActions,
  Button,
  TextField,
  Snackbar,
  Alert
} from '@mui/material';
import { Edit, MoreVert, Cancel, Save } from '@mui/icons-material';
import { api } from '../services/api';

export default function TransactionActions({ transaction, refreshData }) {
  const [anchorEl, setAnchorEl] = useState(null);
  const [openEditDialog, setOpenEditDialog] = useState(false);
  const [openCancelDialog, setOpenCancelDialog] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });
  const [editedTransaction, setEditedTransaction] = useState({ ...transaction });

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleEditClick = () => {
    setOpenEditDialog(true);
    handleMenuClose();
  };

  const handleCancelClick = () => {
    setOpenCancelDialog(true);
    handleMenuClose();
  };

  const handleEditSubmit = async () => {
    try {
      // Create a new version of the transaction
      const newTransaction = {
        ...editedTransaction,
        version: transaction.version,
        action: 'UPDATE'
      };
      
      await api.createTransaction(newTransaction);
      setSnackbar({ open: true, message: 'Transaction updated successfully', severity: 'success' });
      refreshData();
      setOpenEditDialog(false);
    } catch (error) {
      setSnackbar({ open: true, message: 'Error updating transaction', severity: 'error' });
      console.error('Error updating transaction:', error);
    }
  };

  const handleCancelTransaction = async () => {
    try {
      // Create a cancellation transaction
      const cancelTransaction = {
        ...transaction,
        version: transaction.version + 1,
        action: 'CANCEL'
      };
      
      await api.createTransaction(cancelTransaction);
      setSnackbar({ open: true, message: 'Transaction canceled successfully', severity: 'success' });
      refreshData();
      setOpenCancelDialog(false);
    } catch (error) {
      setSnackbar({ open: true, message: 'Error canceling transaction', severity: 'error' });
      console.error('Error canceling transaction:', error);
    }
  };

  const handleFieldChange = (field) => (event) => {
    setEditedTransaction({
      ...editedTransaction,
      [field]: field === 'quantity' ? parseInt(event.target.value) : event.target.value
    });
  };

  return (
    <>
      <IconButton
        aria-label="transaction actions"
        aria-controls="transaction-actions"
        aria-haspopup="true"
        onClick={handleMenuOpen}
      >
        <MoreVert />
      </IconButton>
      
      <Menu
        id="transaction-actions"
        anchorEl={anchorEl}
        keepMounted
        open={Boolean(anchorEl)}
        onClose={handleMenuClose}
      >
        <MenuItem onClick={handleEditClick}>
          <Edit fontSize="small" sx={{ mr: 1 }} /> Edit
        </MenuItem>
        <MenuItem onClick={handleCancelClick}>
          <Cancel fontSize="small" sx={{ mr: 1 }} /> Cancel
        </MenuItem>
      </Menu>

      {/* Edit Dialog */}
      <Dialog open={openEditDialog} onClose={() => setOpenEditDialog(false)}>
        <DialogTitle>Edit Transaction</DialogTitle>
        <DialogContent>
          <TextField
            margin="dense"
            label="Security Code"
            fullWidth
            variant="outlined"
            value={editedTransaction.securityCode}
            onChange={handleFieldChange('securityCode')}
            sx={{ mb: 2 }}
          />
          <TextField
            margin="dense"
            label="Quantity"
            type="number"
            fullWidth
            variant="outlined"
            value={editedTransaction.quantity}
            onChange={handleFieldChange('quantity')}
            sx={{ mb: 2 }}
          />
          <TextField
            margin="dense"
            label="Trade Type"
            select
            fullWidth
            variant="outlined"
            value={editedTransaction.tradeType}
            onChange={handleFieldChange('tradeType')}
          >
            <MenuItem value="BUY">Buy</MenuItem>
            <MenuItem value="SELL">Sell</MenuItem>
          </TextField>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenEditDialog(false)}>Cancel</Button>
          <Button 
            onClick={handleEditSubmit}
            variant="contained"
            startIcon={<Save />}
          >
            Save Changes
          </Button>
        </DialogActions>
      </Dialog>

      {/* Cancel Confirmation Dialog */}
      <Dialog open={openCancelDialog} onClose={() => setOpenCancelDialog(false)}>
        <DialogTitle>Confirm Cancellation</DialogTitle>
        <DialogContent>
          Are you sure you want to cancel this transaction? This will create a new cancellation record.
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenCancelDialog(false)}>No</Button>
          <Button 
            onClick={handleCancelTransaction}
            variant="contained"
            color="error"
            startIcon={<Cancel />}
          >
            Confirm Cancel
          </Button>
        </DialogActions>
      </Dialog>

      {/* Snackbar for feedback */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
      >
        <Alert 
          onClose={() => setSnackbar({ ...snackbar, open: false })}
          severity={snackbar.severity}
          sx={{ width: '100%' }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </>
  );
}