import { useContext } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "./AuthContext"

export default function ProtectedRoute({ children }) {
  const { userIsAuthenticated } = useContext(AuthContext);

  if (userIsAuthenticated) return <Navigate to="/dashboard" />;

  return children;
}