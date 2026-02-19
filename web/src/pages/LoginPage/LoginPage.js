import AuthRegisterFormContainer from "../../components/AuthRegisterFormContainer/AuthRegisterFormContainer";
import { useRef, useState, useEffect, useContext } from "react";
import "./LoginPage.css";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../AuthManagement/AuthContext";




export default function LoginPage() {

    // useNavigate and useContext
    const navigate = useNavigate();
    const { setUser, setUserIsAuthenticated, userIsAuthenticated } = useContext(AuthContext);

    // useStates
    const [errorMsg, setErrorMsg] = useState("");

    const [fieldValidationTracker, setFieldsValidationTracker] = useState({
        usernameIsValid: false,
        passwordIsValid: false,
    });



    // useRefs
    const usernameField = useRef(null);
    const passwordField = useRef(null);
    


    // useEffects
    useEffect(() => {
        console.log(userIsAuthenticated);
    }, []);



    //helper functions
    function onUsernameInput() {
        if (usernameField.current.value != "") {
            setFieldsValidationTracker(prev => ({ ...prev, usernameIsValid: true }));
        }
    }

    function onPasswordInput() {
        if (passwordField.current.value != "") {
            setFieldsValidationTracker(prev => ({ ...prev, passwordIsValid: true }));
        }
    }

    function clearForm() {
        setErrorMsg({
            username: "",
            password: "",
        })

        usernameField.current.value = "";
        passwordField.current.value = "";
    }



    // JSX/Api calls
    async function handleOnSubmit(e) {
        e.preventDefault();

        const loginFormData = {
            username: usernameField.current.value,
            password: passwordField.current.value,
        };

        try {
            const response = await fetch('http://localhost:8080/login', {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(loginFormData)
            });

            const data = await response.json();

            if (!response.ok) {
                setErrorMsg(data.message);
                return; 
            }

            localStorage.setItem("token", data.token);

            const userRes = await fetch('http://localhost:8080/me', {
                headers: { "Authorization": `Bearer ${data.token}` }
            });
            const userData = await userRes.json();
            setUser(userData);
            setUserIsAuthenticated(true);

            navigate("/dashboard");

        } catch (err) {
            console.error("Login Error:", err);
            setErrorMsg({ password: "Something went wrong. Try again." });
        }
    }







    return (
        <div className="login-page">
            <AuthRegisterFormContainer formPurpose="login">
                <div className="container-content">
                    <p style={{
                        fontSize: "2rem",
                        fontWeight: "700",
                        marginBottom: ".5rem",
                    }}>Sign in</p>

                    <form className="form" >

                        <div className="input-group">
                            <p>Username</p>
                            <input ref={usernameField} type="text" placeholder="Username"
                                style={ 
                                    errorMsg!="" ? {boxShadow: "inset 0 0 0 1px #ff0000"} : {}
                                }
                                onChange={onUsernameInput}
                            />
                        </div> 

                        <div className="input-group">
                            <p>Password</p>
                            <input ref={passwordField} type="password" placeholder="Password"
                                style={ 
                                    errorMsg!="" ? {boxShadow: "inset 0 0 0 1px #ff0000"} : {}
                                }
                                onChange={onPasswordInput}
                            />
                            <p className="error-message">{errorMsg}</p>
                        </div> 

                        <button type="submit" 
                        disabled={!(
                            fieldValidationTracker.usernameIsValid &&
                            fieldValidationTracker.passwordIsValid
                        )}
                        onClick={handleOnSubmit}>Sign in</button>

                    </form>
                </div>
                
            </AuthRegisterFormContainer>
        </div>
    );
}