import AuthRegisterFormContainer from "../../components/AuthRegisterFormContainer/AuthRegisterFormContainer";
import { useRef, useState, useEffect } from "react";
import "./LoginPage.css";




export default function LoginPage() {

    // useStates
    const [errorMsgs, setErrorMsgs] = useState({
        username: "",
        password: "",
    });

    const [fieldTouchTracker, setFieldTouchTracker] = useState({
        usernameIsTouched: false,
        passwordIsTouched: false,
    });

    const [fieldValidationTracker, setFieldsValidationTracker] = useState({
        usernameIsValid: false,
        passwordIsValid: false,
    });



    // useRefs
    const usernameField = useRef(null);
    const passwordField = useRef(null);
    


    // useEffects
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

    }, [fieldTouchTracker])



    //helper functions
    function onUsernameInput() {
        setErrorMsgs(prev => ({
            ...prev,
            username: "",
        }))
        setFieldsValidationTracker(prev => ({
            ...prev,
            usernameIsValid: true,
        }))
    }

    function onPasswordInput() {
        setErrorMsgs(prev => ({
            ...prev,
            password: "",
        }))
        setFieldsValidationTracker(prev => ({
            ...prev,
            passwordIsValid: true,
        }))
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
                            <p>Password</p>
                            <input ref={passwordField} type="password" placeholder="Password"
                                style={ 
                                    errorMsgs.password!="" ? {boxShadow: "inset 0 0 0 1px #ff0000"} : {}
                                }
                                onChange={onPasswordInput}
                                onBlur={() => {
                                    setFieldTouchTracker(prev => ({...prev, passwordIsTouched: true}))
                                }}
                            />
                            <p className="error-message">{errorMsgs.password}</p>
                        </div> 

                        <button type="button" disabled={!(fieldValidationTracker.usernameIsValid &&
                                                        fieldValidationTracker.passwordIsValid
                        )}>Sign in</button>

                    </div>
                </div>
                
            </AuthRegisterFormContainer>
        </div>
    );
}