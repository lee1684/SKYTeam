import { Avatar, Menu, MenuProps } from 'antd';
import { Outlet, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import avatar from '../../assets/images/avatar.png';
import { Header } from 'antd/es/layout/layout';
import { UserOutlined, AlertOutlined } from '@ant-design/icons';
import { textStyles } from '../../assets/textStyles';
type MenuItem = Required<MenuProps>['items'][number];

const items: MenuItem[] = [
  {
    key: 'sub1',
    label: '고객관리',
    icon: <UserOutlined />,
    children: [
      {
        key: 'g1',
        label: '유저',
        type: 'group',

        children: [
          { key: '/user-list', label: '전체 목록' },
          { key: '2', label: '블랙리스트' },
        ],
      },
      {
        key: 'g2',
        label: '모임',
        type: 'group',
        children: [{ key: '/moim-list', label: '전체 목록' }],
      },
    ],
  },
  {
    key: 'sub2',
    label: '신고관리',
    icon: <AlertOutlined />,
    children: [{ key: '4', label: '전체 목록 ' }],
  },
];

const Container = styled.div`
  display: flex;
  background-color: #f0f2f5;
`;

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
`;

const Text = styled.div`
  ${textStyles.medium}
`;

export default function Layout() {
  const navigate = useNavigate();

  const onClick: MenuProps['onClick'] = (e) => {
    navigate(e.key);
  };

  return (
    <Container>
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          backgroundColor: '#001529',
          width: 256,
          height: '100vh',
          position: 'sticky',
          top: 0,
        }}
      >
        <Text
          style={{
            height: 64,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            fontSize: 24,
            fontWeight: 'bold',
          }}
        >
          SSALON
        </Text>
        <div
          style={{
            height: 128,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            backgroundColor: 'rgba(255, 255, 255, 0.1)',
            gap: 32,
          }}
        >
          <Avatar size={64} src={avatar} />
          <div
            style={{
              display: 'flex',
              flexDirection: 'column',
              gap: 8,
            }}
          >
            <Text
              style={{
                fontSize: 16,
                fontWeight: 'bold',
                color: 'white',
              }}
            >
              임시 관리자
            </Text>
            <div
              style={{
                display: 'flex',
                gap: 4,
                alignItems: 'center',
              }}
            >
              <div
                style={{
                  backgroundColor: 'rgba(0, 255, 0, 0.25)',
                  borderRadius: '50%',
                  width: 16,
                  height: 16,
                }}
              />
              <Text
                style={{
                  color: 'green',
                }}
              >
                Online
              </Text>
            </div>
          </div>
        </div>
        <Menu
          style={{
            flex: 1,
          }}
          onClick={onClick}
          theme='dark'
          mode='inline'
          defaultSelectedKeys={['/' + window.location.pathname.split('/')[1]]}
          defaultOpenKeys={['sub1']}
          items={items}
        />
      </div>
      <Wrapper>
        <Header />
        <Outlet />
      </Wrapper>
    </Container>
  );
}
