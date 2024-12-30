import axios from "axios";

const apiClient=axios.create({
    baseUrl: 'http://localhost:8080',
});

apiClient.interceptors.request.use(
    (config) =>{
        const token=sessionStorage.getItem("jwtToken");
        if(token){
            config.headers['Authorization']=token;
            console.log(token);
        }
        return config;
    },
    (error)=>{
        return Promise.reject(error);
    }
);

export default apiClient;