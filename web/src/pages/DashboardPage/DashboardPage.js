import { useContext } from "react";
import { AuthContext } from "../AuthManagement/AuthContext";

export default function DashboardPage() {

    const { userIsAuthenticated } = useContext(AuthContext);

    return (
        <div>
            <h1>Dashboard</h1>
        </div>
    )
}