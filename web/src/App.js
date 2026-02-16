import './App.css';
import "@fontsource/dm-sans";
import "@fontsource/dm-sans/400.css";
import "@fontsource/dm-sans/500.css";
import "@fontsource/dm-sans/700.css";
import RegisterPage from './pages/RegisterPage/RegisterPage';
import LoginPage from './pages/LoginPage/LoginPage';
import DashboardPage from './pages/DashboardPage/DashboardPage';
import ProtectedRoute from './pages/AuthManagement/ProtectedRoute';
import PublicRoute from './pages/AuthManagement/PublicRoute';
import { BrowserRouter as Router, Routes, Route, Navigate, data } from "react-router-dom";
import { AuthProvider } from './pages/AuthManagement/AuthContext';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>

          <Route path="*" element={<Navigate to="/login" />} />

          <Route path="/login" element={
            <PublicRoute><LoginPage /></PublicRoute>
          } />

          <Route path="/register" element={
            <PublicRoute><RegisterPage /></PublicRoute>
          } />
          
          <Route path="/dashboard" element={
            <ProtectedRoute><DashboardPage /></ProtectedRoute>
          } />

        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
