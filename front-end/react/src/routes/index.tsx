import { createBrowserRouter } from 'react-router-dom';
import { AuthRoutes } from './Auth';
import { HomeRoutes } from './Home';

export const router = createBrowserRouter([
  {
    path: '/',
    errorElement: <div>404 Not Found</div>,
    children: [HomeRoutes, AuthRoutes],
  },
]);
