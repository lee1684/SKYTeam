import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { RouterProvider } from 'react-router-dom';
import { createGlobalStyle } from 'styled-components';
import reset from 'styled-reset';
import { router } from './routes';
import { Spin } from 'antd';

const queryClient = new QueryClient();

const GlobalStyle = createGlobalStyle`
  ${reset}
`;

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <GlobalStyle />
      <RouterProvider router={router} fallbackElement={<Spin />} />
    </QueryClientProvider>
  );
}
