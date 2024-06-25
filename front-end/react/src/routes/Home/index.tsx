import Layout from '../../components/layout/Layout';
import Home from '../../pages/Home';
import MoimList from '../../pages/Home/MoimList';
import UserList from '../../pages/Home/UserList';

export const HomeRoutes = {
  element: <Layout />,
  children: [
    {
      path: '/',
      element: <Home />,
    },
    {
      path: 'user-list',
      element: <UserList />,
    },
    {
      path: 'moim-list',
      element: <MoimList />,
    },
  ],
};
