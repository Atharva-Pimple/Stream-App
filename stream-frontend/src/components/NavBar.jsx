import { Navbar, DarkThemeToggle } from "flowbite-react";
import play from "../assets/play-button.png";
import { useLocation } from "react-router-dom";

function NavBar() {
  const location=useLocation();
  return (
    <Navbar fluid className="bg-gray-800 border-gray-700 w-auto">
      <Navbar.Brand>
        <img src={play} className="mr-3 h-6 sm:h-9" alt="StreamApp" />
        <span className="self-center whitespace-nowrap text-xl font-semibold text-white dark:text-white">
          VStream
        </span>
      </Navbar.Brand>

      <Navbar.Toggle />
      <Navbar.Collapse className="w-full">
        <Navbar.Link href="/videos" active={location.pathname=='/videos' || location.pathname=='/'} className="text-white mt-2">
          Videos
        </Navbar.Link>
        <Navbar.Link href="/about" active={location.pathname=='/about'} className="text-white mt-2">
          About
        </Navbar.Link>
        <Navbar.Link href="/upload" active={location.pathname=='/upload'} className="text-white mt-2">
          Upload Video
        </Navbar.Link>
        <Navbar.Link href="/login" active={location.pathname=='/login'} className="text-white mt-2">
          Login
        </Navbar.Link>
        <div>
          <DarkThemeToggle className="text-white focus:ring-0 hover:bg-cyan-700" />
        </div>
      </Navbar.Collapse>
    </Navbar>
  );
}

export default NavBar;
