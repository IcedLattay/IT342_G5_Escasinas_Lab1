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

        if (!user) fetchUserData();
    }, []);

    

    // JSX/Api calls
    async function fetchUserData() {
        console.log("fetching user data...");

        const token = localStorage.getItem("token")

        const res = await fetch("http://localhost:8080/me", {
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (res.ok) {
            const data = await res.json();
            setUser(data);
        }
    }



    return (
        <AuthContext.Provider value={{ userIsAuthenticated, setUserIsAuthenticated, user, setUser }}>
        {children}
        </AuthContext.Provider>
    );
}
