/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import uploadLogo from "../assets/upload.png";
import { Button, Card, Label, Textarea, TextInput, Progress, Alert} from "flowbite-react";
import axios from "axios";

function VideoUpload() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [meta,setMeta]= useState({
    title:"",
    description:""
  });
  const [progress, setProgress] = useState(0);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState("");

  function handleFieldChange(event){
    // console.log(event.target.name);
    // console.log(event.target.value);
    setMeta({
      ...meta,
      [event.target.name]:event.target.value,
    })
  }

  function handleFileChange(event) {
    // console.log(event.target.files[0]);
    setSelectedFile(event.target.files[0]);
  }

  function handleForm(formEvent){
    formEvent.preventDefault();
    if(!selectedFile){
      alert("Select File");
      return;
    }

    saveVideoToServer(selectedFile,meta);
  }

  function resetForm(){

    setMeta({
      title:"",
      description:""
    });
    setSelectedFile(null);
  }

  async function saveVideoToServer(video,videoMetaData){
    setUploading(true);

    let formData=new FormData();
    formData.append("title",videoMetaData.title);
    formData.append("description",videoMetaData.description);
    formData.append("file",selectedFile);

    try{
      let response=await axios.post(`http://localhost:8080/api/v1/videos`,formData,{
        headers:{
          'Content-Type':'multipart/form-data',
        },
        onUploadProgress:(progresEvent)=>{
          const prog =Math.round((progresEvent.loaded * 100)/ progresEvent.total);
          console.log(prog);
          setProgress(prog);
        }
      });

      console.log(response);

      setProgress(0);
      setUploading(false);
      setMessage("File uploaded");
      resetForm();
    }catch(error){
      console.log(error);
      setUploading(false);
      setMessage("Error in uploading file");
    }
  }

  return (
    <div className="dark:text-white flex justify-center space-y-3 mt-6">
      <Card className=" bg-gray-200 hover:bg-gray-300 border-blue-600 dark:border-blue-500 border-0 border-t-8 rounded-md  max-w-4xl mx-auto">
        <h1 className="font-semibold text-2xl">Upload Videos</h1>
        <div>
          <form className="flex flex-col space-y-5" onSubmit={handleForm}>
            <div>
              <div className="mb-4 block">
                <Label htmlFor="file-upload" value="Video Title" />
              </div>
              <TextInput value={meta.title} onChange={handleFieldChange} name="title" placeholder="Enter title"/>
            </div>
            <div className="max-w-md">
              <div className="mb-2 block">
                <Label htmlFor="comment" value="Video Description" />
              </div>
              <Textarea
              value={meta.description}
                onChange={handleFieldChange}
                name="description"
                id="comment"
                placeholder="Write video description..."
                required
                rows={4}
              />
            </div>
            <div className="flex items-center space-x-6 sm:flex-row">
              <div className="shrink-0">
                <img
                  className="h-16 w-16 object-cover"
                  src={uploadLogo}
                  alt="Current profile photo"
                />
              </div>
              <label className="block">
                <span className="sr-only">Choose profile photo</span>
                <input
                  name="file"
                  onChange={handleFileChange}
                  type="file"
                  className="block w-full text-sm text-slate-500
              file:mr-4 file:py-2 file:px-4
              file:rounded-full file:border-0
              file:text-sm file:font-semibold
              file:bg-violet-50 file:text-violet-700
              hover:file:bg-violet-100"
                />
              </label>
            </div>

            <div className="text-center">
              {uploading && <Progress progress={progress} textLabel="Uploading..." size="lg" labelProgress labelText />}
            </div>

            <div>
              {message && <Alert color={"success"} rounded withBorderAccent onDismiss={()=>{setMessage("")}}>
                <span className="font-medium">Success alert! </span>
                {message}
              </Alert>}
            </div>
            <div className="flex justify-center">
              <Button disabled={uploading} type="submit" className="bg-blue-600">Upload</Button>
            </div>
          </form>
        </div>
      </Card>
    </div>
  );
}

export default VideoUpload;
