// reactstrap components
import {
  Button,
  Card,
  CardHeader,
  CardBody,
  FormGroup,
  Form,
  Input,
  InputGroupAddon,
  InputGroupText,
  InputGroup,
  Row,
  Col,
} from "reactstrap";
import {useState} from "react";
import { useNavigate } from "react-router-dom";

const Register = () => {
  const[id, setId] = useState("");
  const[password, setPassword] = useState("");
  const[username, setUsername] = useState("");
  const[email, setEmail] = useState("");
  const navigate = useNavigate();

  const handleInputId = e => {
    setId(e.target.value);
  }

  const handleInputPassword = e => {
    setPassword(e.target.value);
  }

  const handleInputUsername = e => {
    setUsername(e.target.value);
  }

  const handleInputEmail = e => {
    setEmail(e.target.value);
  }

  const isIdValid = (id) => {
    // 이메일 형식을 확인하는 정규표현식
    const idRegex = /^[a-z]+[a-z0-9]{3,19}$/g;
    
    return idRegex.test(id);
  };

  const isPasswordValid = (password) => {
    // 이메일 형식을 확인하는 정규표현식
    const passwordRegex = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}$/;
    
    return passwordRegex.test(password);
  }; 

  const isUsernameValid = (username) => {
    // 이메일 형식을 확인하는 정규표현식
    const usernameRegex = /^[a-zA-Zㄱ-힣][a-zA-Zㄱ-힣 ]*$/;
    
    return usernameRegex.test(username);
  };

  const isEmailValid = (email) => {
    // 이메일 형식을 확인하는 정규표현식
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    return emailRegex.test(email);
  };

  const registerConfirm = e => {
    e.preventDefault();
    if(!isIdValid(id)) {
      window.alert("아이디는 영문자로 시작하는 영문자 또는 숫자 4~20자여야 합니다!");
    } else if(!isPasswordValid(password)) {
      window.alert("비밀번호는 8 ~ 16자 영문, 숫자 조합이여야 합니다!");
    } else if(!isUsernameValid(username)) {
      window.alert("유저 이름은 한글 또는 영문이여야 합니다!");
    } else if(!isEmailValid(email)) {
      window.alert("올바른 이메일 주소를 입력하세요!");
    } else {
      fetch(`http://${process.env.REACT_APP_GATEWAY}/api/v1/user-service/users`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          id, password, username, email
        }),
      }).then(response => {
        if(response.ok) {
          console.log(response);
          window.alert("회원가입이 완료되었습니다.");
          navigate("/");
        } else {
          window.alert("이미 존재하는 아이디입니다.");
        }
      })
      .catch(e => {
        console.log(e);
      })
    }
  }

  return (
    <>
      <Col lg="6" md="8">
        <Card className="bg-secondary shadow border-0">
          <CardBody className="px-lg-5 py-lg-5">
            <div className="text-center text-muted mb-4">
              <small>Sign up with credentials</small>
            </div>
            <Form role="form">
              <FormGroup>
                <InputGroup className="input-group-alternative mb-3">
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <i className="ni ni-single-02" />
                    </InputGroupText>
                  </InputGroupAddon>
                  <Input placeholder="Id" type="text" onChange={handleInputId} value={id}/>
                </InputGroup>
              </FormGroup>
              <FormGroup>
                <InputGroup className="input-group-alternative">
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <i className="ni ni-lock-circle-open" />
                    </InputGroupText>
                  </InputGroupAddon>
                  <Input
                    placeholder="Password"
                    type="password"
                    autoComplete="new-password"
                    onChange={handleInputPassword} value={password}
                  />
                </InputGroup>
              </FormGroup>
              <FormGroup>
                <InputGroup className="input-group-alternative mb-3">
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <i className="ni ni-hat-3" />
                    </InputGroupText>
                  </InputGroupAddon>
                  <Input placeholder="Username" type="text" onChange={handleInputUsername} value={username}/>
                </InputGroup>
              </FormGroup>
              <FormGroup>
                <InputGroup className="input-group-alternative mb-3">
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <i className="ni ni-email-83" />
                    </InputGroupText>
                  </InputGroupAddon>
                  <Input
                    placeholder="Email"
                    type="email"
                    autoComplete="new-email"
                    onChange={handleInputEmail} value={email}
                  />
                </InputGroup>
              </FormGroup>
              {/* <div className="text-muted font-italic">
                <small>
                  password strength:{" "}
                  <span className="text-success font-weight-700">strong</span>
                </small>
              </div> */}
              {/* <Row className="my-4">
                <Col xs="12">
                  <div className="custom-control custom-control-alternative custom-checkbox">
                    <input
                      className="custom-control-input"
                      id="customCheckRegister"
                      type="checkbox"
                    />
                    <label
                      className="custom-control-label"
                      htmlFor="customCheckRegister"
                    >
                      <span className="text-muted">
                        I agree with the{" "}
                        <a href="#pablo" onClick={(e) => e.preventDefault()}>
                          Privacy Policy
                        </a>
                      </span>
                    </label>
                  </div>
                </Col>
              </Row> */}
              <div className="text-center">
                <Button className="mt-4" color="primary" type="button" onClick={registerConfirm}>
                  Create account
                </Button>
              </div>
            </Form>
          </CardBody>
        </Card>
      </Col>
    </>
  );
};

export default Register;
