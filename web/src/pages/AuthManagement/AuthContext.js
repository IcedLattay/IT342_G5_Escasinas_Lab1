import { createContext, useState, useEffect, use } from "react";

export const AuthContext = createContext();

export function AuthProvider({ children }) {
    
    // useStates
    const [userIsAuthenticated, setUserIsAuthenticated] = useState(null);
    const [user, setUser] = useState(null);



    // useEffects
    useEffect(() => {
        const token = localStorage.getItem("token");
        
        if (token) {
        setUserIsAuthenticated(true);
        } else {
        setUserIsAuthenticated(false);
        }
    }, []);

    return (
        <AuthContext.Provider value={{ userIsAuthenticated, setUserIsAuthenticated, user, setUser }}>
        {children}
        </AuthContext.Provider>
    );
}
