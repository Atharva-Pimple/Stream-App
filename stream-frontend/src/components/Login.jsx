import { Button, Card, Label, TextInput, Alert} from "flowbite-react";
import axios from "axios";
import { useState } from "react";

function Login() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [message, setMessage] = useState("");
  const [alertColor, setAlertColor] = useState("failure");

  const handleOnChange = (e) => {
    const { id, value } = e.target;
    setFormData({
      ...formData,
      [id]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        "http://localhost:8080/auth/login",
        formData
      );
      setMessage(response.data.message);
      if(response.data.success){
        setAlertColor("success")
      }
      console.log(response.headers['authorization']);
      if(response.headers['authorization']){
        const token=response.headers['authorization'];
        sessionStorage.setItem("jwtToken",token);
      }
    } catch (error) {
        if(error.response.data.message){
            setMessage(error.response.data.message);
        }else{
            setMessage("Login failed!");
        }
      console.log(error);
    }
  };

  return (
    <div className="dark:text-white flex justify-center space-y-3 mt-6 ">
    <Card className="max-w-sm justify-center w-full">
      {message && (
        <Alert
          color={alertColor}
          rounded
          withBorderAccent
          onDismiss={() => {
            setMessage("");
          }}
        >
          {message}
        </Alert>
      )}
      <form className="flex flex-col gap-4" onSubmit={handleSubmit}>
        <div>
          <div className="mb-2 block">
            <Label htmlFor="email" value="Your email" />
          </div>
          <TextInput
            id="email"
            type="email"
            placeholder="johndoe@.com"
            value={formData.email}
            onChange={handleOnChange}
            required
          />
        </div>
        <div>
          <div className="mb-2 block">
            <Label htmlFor="password" value="Your password" />
          </div>
          <TextInput
            id="password"
            type="password"
            value={formData.password}
            onChange={handleOnChange}
            required
          />
        </div>
        <div className="text-center">
          <p className="font-medium">New User? <a href="/signup" className="font-light text-blue-600">Register yourself.</a></p>
        </div>
        <Button type="submit">Login</Button>
      </form>
    </Card>
    </div>
  );
}

export default Login;
