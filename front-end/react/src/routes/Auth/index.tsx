import SignIn from '../../pages/Auth/SignIn';

export const AuthRoutes = {
  children: [
    {
      path: 'sign-in',
      element: <SignIn />,
    },
  ],
};
