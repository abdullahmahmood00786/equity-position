import React from 'react';
import { 
  Table, 
  TableBody, 
  TableCell, 
  TableContainer, 
  TableHead, 
  TableRow, 
  Paper, 
  Typography,
  CircularProgress,
  Box
} from '@mui/material';

export default function PositionsView({ positions, loading }) {
  return (
    <Paper sx={{ p: 3 }}>
      <Typography variant="h6" gutterBottom>
        Current Positions
      </Typography>
      
      {loading ? (
        <Box display="flex" justifyContent="center">
          <CircularProgress />
        </Box>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Security Code</TableCell>
                <TableCell align="right">Position</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {positions.map((position) => (
                <TableRow key={position.securityCode}>
                  <TableCell component="th" scope="row">
                    {position.securityCode}
                  </TableCell>
                  <TableCell align="right" sx={{ color: position.quantity >= 0 ? 'success.main' : 'error.main' }}>
                    {position.quantity >= 0 ? '+' : ''}{position.quantity}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </Paper>
  );
}