import { Menu, MenuProps } from 'antd';
import { Outlet } from 'react-router-dom';
import styled from 'styled-components';

type MenuItem = Required<MenuProps>['items'][number];

const items: MenuItem[] = [
  {
    key: 'sub1',
    label: '고객관리',
    children: [
      {
        key: 'g1',
        label: '유저',
        type: 'group',
        children: [
          { key: '1', label: '전체 목록' },
          { key: '2', label: '블랙리스트' },
        ],
      },
      {
        key: 'g2',
        label: '모임',
        type: 'group',
        children: [{ key: '3', label: '전체 목록' }],
      },
    ],
  },
  {
    key: 'sub2',
    label: '신고관리',
    children: [{ key: '4', label: '전체 목록 ' }],
  },
];

const Container = styled.div`
  display: flex;
`;

export default function Layout() {
  const onClick: MenuProps['onClick'] = (e) => {
    console.log(e);
  };

  return (
    <Container>
      <Menu
        onClick={onClick}
        style={{
          width: 256,
          height: '100vh',
          position: 'sticky',
          top: 0,
        }}
        theme='dark'
        mode='inline'
        defaultSelectedKeys={['1']}
        defaultOpenKeys={['sub1']}
        items={items}
      />
      <Outlet />;
    </Container>
  );
}
