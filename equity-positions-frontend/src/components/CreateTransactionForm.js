import React from 'react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { 
  Button, 
  TextField, 
  MenuItem, 
  Grid, 
  Paper, 
  Typography,
  CircularProgress
} from '@mui/material';
import { api } from '../services/api';

const validationSchema = Yup.object({
  tradeId: Yup.number().required('Trade ID is required').positive(),
  version: Yup.number().required('Version is required').positive().integer(),
  securityCode: Yup.string().required('Security code is required').max(10),
  quantity: Yup.number().required('Quantity is required').positive().integer(),
  action: Yup.string().required('Action is required'),
  tradeType: Yup.string().required('Trade type is required')
});

export default function CreateTransactionForm({ onTransactionCreated }) {
  const formik = useFormik({
    initialValues: {
      tradeId: '',
      version: '1',
      securityCode: '',
      quantity: '',
      action: 'INSERT',
      tradeType: 'BUY'
    },
    validationSchema: validationSchema,
    onSubmit: async (values, { setSubmitting, resetForm }) => {
      try {
        const response = await api.createTransaction(values);
        onTransactionCreated(response.data);
        resetForm();
      } catch (error) {
        console.error('Error creating transaction:', error);
      } finally {
        setSubmitting(false);
      }
    },
  });

  return (
    <Paper sx={{ p: 3 }}>
      <Typography variant="h6" gutterBottom>
        Create New Transaction
      </Typography>
      
      <form onSubmit={formik.handleSubmit}>
        <Grid container spacing={3}>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              id="tradeId"
              name="tradeId"
              label="Trade ID"
              type="number"
              value={formik.values.tradeId}
              onChange={formik.handleChange}
              error={formik.touched.tradeId && Boolean(formik.errors.tradeId)}
              helperText={formik.touched.tradeId && formik.errors.tradeId}
            />
          </Grid>
          
          <Grid item xs={12} sm={6}>
            <TextField
              select
              fullWidth
              id="version"
              name="version"
              label="Version"
              type="number"
              value={formik.values.version}
              onChange={formik.handleChange}
              error={formik.touched.version && Boolean(formik.errors.version)}
              helperText={formik.touched.version && formik.errors.version}
            >
              <MenuItem value="1">1</MenuItem>
            </TextField>
          </Grid>
          
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              id="securityCode"
              name="securityCode"
              label="Security Code"
              value={formik.values.securityCode}
              onChange={formik.handleChange}
              error={formik.touched.securityCode && Boolean(formik.errors.securityCode)}
              helperText={formik.touched.securityCode && formik.errors.securityCode}
            />
          </Grid>
          
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              id="quantity"
              name="quantity"
              label="Quantity"
              type="number"
              value={formik.values.quantity}
              onChange={formik.handleChange}
              error={formik.touched.quantity && Boolean(formik.errors.quantity)}
              helperText={formik.touched.quantity && formik.errors.quantity}
            />
          </Grid>
          
          <Grid item xs={12} sm={6}>
            <TextField
              select
              fullWidth
              id="action"
              name="action"
              label="Action"
              value={formik.values.action}
              onChange={formik.handleChange}
              error={formik.touched.action && Boolean(formik.errors.action)}
              helperText={formik.touched.action && formik.errors.action}
            >
              <MenuItem value="INSERT">Insert</MenuItem>
            </TextField>
          </Grid>
          
          <Grid item xs={12} sm={6}>
            <TextField
              select
              fullWidth
              id="tradeType"
              name="tradeType"
              label="Trade Type"
              value={formik.values.tradeType}
              onChange={formik.handleChange}
              error={formik.touched.tradeType && Boolean(formik.errors.tradeType)}
              helperText={formik.touched.tradeType && formik.errors.tradeType}
            >
              <MenuItem value="BUY">Buy</MenuItem>
              <MenuItem value="SELL">Sell</MenuItem>
            </TextField>
          </Grid>
          
          <Grid item xs={12}>
            <Button 
              color="primary" 
              variant="contained" 
              type="submit"
              disabled={formik.isSubmitting}
              startIcon={formik.isSubmitting ? <CircularProgress size={20} /> : null}
            >
              Create Transaction
            </Button>
          </Grid>
        </Grid>
      </form>
    </Paper>
  );
}