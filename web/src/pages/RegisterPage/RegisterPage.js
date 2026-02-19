import "./RegisterPage.css";
import { useRef, useState, useEffect, useContext } from "react";
import AuthRegisterFormContainer from "../../components/AuthRegisterFormContainer/AuthRegisterFormContainer";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../AuthManagement/AuthContext";


export default function RegisterPage() {
    
    // useNavigate and useContext
    const navigate = useNavigate();
    const { setUser, setUserIsAuthenticated, userIsAuthenticated } = useContext(AuthContext);


    
    // useStates
    const [errorMsgs, setErrorMsgs] = useState({
        username: "",
        email: "",
        password: "",
        confirmPassword: "",
    });

    const [fieldTouchTracker, setFieldTouchTracker] = useState({
        usernameIsTouched: false,
        emailIsTouched: false,
        passwordIsTouched: false,
        confirmPasswordIsTouched: false,
    });
    
    const [fieldValidationTracker, setFieldsValidationTracker] = useState({
        usernameIsValid: false,
        emailIsValid: false,
        passwordIsValid: false,
        confirmPasswordIsValid: false,
    });




    //useRefs
    const usernameField = useRef(null);
    const emailField = useRef(null);
    const passwordField = useRef(null);
    const confirmPasswordField = useRef(null);



    //useEffects
    useEffect(() => {
        
        if (fieldTouchTracker.usernameIsTouched &&
            usernameField.current.value==""
        ) {
            setErrorMsgs(prev => ({
                ...prev,
                username: "This input is required.",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                usernameIsValid: false,
            }))
        }

        if (fieldTouchTracker.emailIsTouched &&
            emailField.current.value==""
        ) {
            setErrorMsgs(prev => ({
                ...prev,
                email: "This input is required.",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                emailIsValid: false,
            }))
        }

        if (fieldTouchTracker.passwordIsTouched &&
            passwordField.current.value==""
        ) {
            setErrorMsgs(prev => ({
                ...prev,
                password: "This input is required.",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                passwordIsValid: false,
            }))
        }

        if (fieldTouchTracker.confirmPasswordIsTouched &&
            confirmPasswordField.current.value==""
        ) {
            setErrorMsgs(prev => ({
                ...prev,
                confirmPassword: "This input is required.",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                confirmPasswordIsValid: false,
            }))
        }

    }, [fieldTouchTracker])




    //helper functions
    function onUsernameInput() {
        if (usernameField.current.value.length < 3 || usernameField.current.value.length > 20) {
            setErrorMsgs(prev => ({
                ...prev,
                username: "Must be between 3 and 20 characters long.",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                usernameIsValid: false,
            }))
        } else if (!/^[a-zA-Z0-9_]+$/.test(usernameField.current.value)) {
            setErrorMsgs(prev => ({
                ...prev,
                username: "Must only contain letters, numbers, and '_'.",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                usernameIsValid: false,
            }))
        } else {
            setErrorMsgs(prev => ({
                ...prev,
                username: "",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                usernameIsValid: true,
            }))
        }
    }

    function onEmailInput() {
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailField.current.value)) {
            setErrorMsgs(prev => ({
                ...prev,
                email: "Invalid email address.",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                emailIsValid: false,
            }))
        } else {
            setErrorMsgs(prev => ({
                ...prev,
                email: "",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                emailIsValid: true,
            }))
        }
    }

    function onPasswordInput() {
        if (passwordField.current.value.length < 8) {
            setErrorMsgs(prev => ({
                ...prev,
                password: "Must be at least 8 characters long.",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                passwordIsValid: false,
            }))
        } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(passwordField.current.value)) {
            setErrorMsgs(prev => ({
                ...prev,
                password: "Must contain at least one uppercase letter, one lowercase letter, and one number.",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                passwordIsValid: false,
            }))
        } else {
            setErrorMsgs(prev => ({
                ...prev,
                password: "",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                passwordIsValid: true,
            }))
        }
    }

    function onConfirmPasswordInput() {
        if (confirmPasswordField.current.value=="") return;

        if (passwordField.current.value=="") {
            setErrorMsgs(prev => ({
                ...prev,
                confirmPassword: "Please set a password first.",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                confirmPasswordIsValid: false,
            }))
            return;
        }

        if (confirmPasswordField.current.value !== passwordField.current.value) {
            setErrorMsgs(prev => ({
                ...prev,
                confirmPassword: "Passwords do not match.",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                confirmPasswordIsValid: false,
            }))
        } else {
            setErrorMsgs(prev => ({
                ...prev,
                confirmPassword: "",
            }))
            setFieldsValidationTracker(prev => ({
                ...prev,
                confirmPasswordIsValid: true,
            }))
        }
    }



    
    // JSX/Api calls
    async function handleOnSubmit(e) {
        e.preventDefault();

        const registerFormData = {
            username: usernameField.current.value,
            emailAddress: emailField.current.value,
            password: passwordField.current.value,
        };

        try {
            const response = await fetch('http://localhost:8080/register', {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(registerFormData)
            });

            
            const data = await response.json();
            console.log("JWT received:", data.message);

            const token = data.message;

            if (!response.ok) {
                setErrorMsgs(prev => ({
                    ...prev,
                    username: data.message
                }));
            }

            
            localStorage.setItem("token", token);

            const userRes = await fetch('http://localhost:8080/me', {
                headers: { "Authorization": `Bearer ${token}` }
            });
            const userData = await userRes.json();
            setUser(userData);
            setUserIsAuthenticated(true);

            navigate("/dashboard");

        } catch (err) {
            console.error("Register Error:", err);
        }
    }

    

    return (
        <div className="register-page">
            <AuthRegisterFormContainer formPurpose="register">
                <div className="container-content">
                    <p style={{
                        fontSize: "2rem",
                        fontWeight: "700",
                        marginBottom: ".5rem",
                    }}>Sign up</p>
                    <div className="form">
                        <div className="input-group">
                            <p>Username</p>
                            <input ref={usernameField} type="text" placeholder="Username"
                                style={ 
                                    errorMsgs.username!="" ? {boxShadow: "inset 0 0 0 1px #ff0000"} : {}
                                }
                                onChange={onUsernameInput}
                                onBlur={() => {
                                    setFieldTouchTracker(prev => ({...prev, usernameIsTouched: true}))
                                }}
                            />
                            <p className="error-message">{errorMsgs.username}</p>
                            
                        </div> 
                        <div className="input-group">
                            <p>Email Address</p>
                            <input ref={emailField} type="text" placeholder="example@example.com" 
                                style={ 
                                    errorMsgs.email!="" ? {boxShadow: "inset 0 0 0 1px #ff0000"} : {}
                                }
                                onChange={onEmailInput}
                                onBlur={() => {
                                    setFieldTouchTracker(prev => ({...prev, emailIsTouched: true}))
                                }}
                            />
                            <p className="error-message">{errorMsgs.email}</p>

                        </div>
                        <div className="input-group">
                            <p>Password</p>
                            <input ref={passwordField} type="password" placeholder="••••••••"  
                                style={ 
                                    errorMsgs.password!="" ? {boxShadow: "inset 0 0 0 1px #ff0000"} : {}
                                }
                                onChange={onPasswordInput}
                                onBlur={() => {
                                    setFieldTouchTracker(prev => ({...prev, passwordIsTouched: true}))
                                    onConfirmPasswordInput();                                    
                                }}
                            />
                            <p className="error-message">{errorMsgs.password}</p>

                        </div>
                        <div className="input-group">
                            <p>Confirm Password</p>
                            <input ref={confirmPasswordField} type="password" placeholder="••••••••" 
                                style={ 
                                    errorMsgs.confirmPassword!="" ? {boxShadow: "inset 0 0 0 1px #ff0000"} : {}
                                }
                                onChange={onConfirmPasswordInput}
                                onBlur={() => {
                                    setFieldTouchTracker(prev => ({...prev, confirmPasswordIsTouched: true}))
                                }}
                            />
                            <p className="error-message">{errorMsgs.confirmPassword}</p>

                        </div>

                        <button type="button" disabled={!(fieldValidationTracker.usernameIsValid &&
                                                        fieldValidationTracker.emailIsValid &&
                                                        fieldValidationTracker.passwordIsValid && 
                                                        fieldValidationTracker.confirmPasswordIsValid
                        )}
                        onClick={handleOnSubmit}>Sign up</button>
                    </div>
                </div>
            </AuthRegisterFormContainer>
        </div>
    );
}