import "./AuthRegisterFormContainer.css";
import { Link } from "react-router-dom";

export default function AuthRegisterFormContainer({ 
    children,
    formPurpose,
}) {
    return (
        <div className="auth-register-form-container">
            <p style={{
                fontSize: "1.5rem",
                fontWeight: "bolder",
            }}>Mini App</p>
            <div className="card">  
                {children}
            </div>
            <div className="form-footer">

                {formPurpose=="register" && 
                <p style={{
                    display: "flex",
                    gap: ".25rem"
                }}>Already have an account? 
                    <Link to={"/login"}>
                        <span>Sign in</span>
                    </Link>
                </p>
                }

                {formPurpose=="login" && 
                <p style={{
                    display: "flex",
                    gap: ".25rem"
                }}>Don't have an account?
                    <Link to={"/register"}>
                        <span>Sign up</span>
                    </Link>
                </p>
                }
                
            </div>
        </div>
    );
}
