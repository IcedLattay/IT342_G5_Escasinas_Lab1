import { createContext, useState, useEffect, use } from "react";

export const AuthContext = createContext();

export function AuthProvider({ children }) {
    
    // useStates
    const [userIsAuthenticated, setIsAuthenticated] = useState(true);
    

    return (
        <AuthContext.Provider value={{ userIsAuthenticated, setIsAuthenticated }}>
        {children}
        </AuthContext.Provider>
    );
}
