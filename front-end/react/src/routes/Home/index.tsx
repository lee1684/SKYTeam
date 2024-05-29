import Layout from '../../components/layout/Layout';
import Home from '../../pages/Home';

export const HomeRoutes = {
  element: <Layout />,
  children: [
    {
      path: '/',
      element: <Home />,
    },
  ],
};
