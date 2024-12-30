import { Button, Card, Label, TextInput, Alert } from "flowbite-react";
import axios from "axios";
import { useState } from "react";

function Register() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
  });
  const [errors, setErrors] = useState({});
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
        "http://localhost:8080/auth/register",
        formData
      );
      setMessage(response.data.message);
      if (response.data.success) {
        setAlertColor("success");
      }
    } catch (error) {
      if (error.response.data.errors) {
        setErrors(error.response.data.errors);
      }else if (error.response.data.message) {
        setMessage(error.response.data.message);
      } else {
        setMessage("Unexpected Error!");
      }
      console.log(error);
      console.log(error.response.data.errors);
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
              <Label htmlFor="name" value="Your name" />
            </div>
            <TextInput
              id="name"
              type="text"
              placeholder="john Doe"
              value={formData.name}
              onChange={handleOnChange}
              required
            />
            {errors.name && <p style={{ color: 'red' }}>{errors.name}</p>}
          </div>
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
            {errors.email && <p style={{ color: 'red' }}>{errors.email}</p>}
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
            {errors.password && <p style={{ color: 'red' }}>{errors.password}</p>}
          </div>
          <Button type="submit" className="bg-blue-600">
            SignUp
          </Button>
        </form>
      </Card>
    </div>
  );
}

export default Register;
