import styled from 'styled-components';
<<<<<<< HEAD
import { Card, Space, Table } from 'antd';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { UserApi } from '../../apis/user';
=======
import { Card, Table } from 'antd';
>>>>>>> develop

const Container = styled.div`
  padding: 32px;
`;

<<<<<<< HEAD
export default function UserList() {
  const queryClient = useQueryClient();

  const { data } = useQuery({
    queryKey: ['users'],
    queryFn: UserApi.getUserList,
  });

  const { mutate: deleteUser } = useMutation({
    mutationFn: UserApi.deleteUser,
    onSuccess: () => {
      void queryClient.invalidateQueries({
        queryKey: ['users'],
      });
    },
  });

=======
interface DataType {
  nickname: string;
  profilePictureUrl: string;
  gender: string;
  address: string;
  role: string;
  introduction: string;
  interests: string[];
  joinDate: string;
}

const data: DataType = [];

export default function UserList() {
>>>>>>> develop
  return (
    <Container>
      <Card title='유저 전체 목록' style={{ width: '100%' }}>
        <Table dataSource={data}>
<<<<<<< HEAD
          <Table.Column
            title='닉네임'
            dataIndex='nickname'
            key='nickname'
            width={100}
          />
=======
          <Table.Column title='닉네임' dataIndex='nickname' key='nickname' />
>>>>>>> develop
          <Table.Column
            title='프로필 사진'
            dataIndex='profilePictureUrl'
            key='profilePictureUrl'
<<<<<<< HEAD
            width={150}
            render={(url) => {
              return <img src={url} width={50} height={50} alt='프로필' />;
            }}
          />
          <Table.Column
            title='성별'
            dataIndex='gender'
            key='gender'
            width={100}
          />
          <Table.Column
            title='주소'
            dataIndex='address'
            key='address'
            width={100}
          />

=======
          />
          <Table.Column title='성별' dataIndex='gender' key='gender' />
          <Table.Column title='주소' dataIndex='address' key='address' />
          <Table.Column title='역할' dataIndex='role' key='role' />
>>>>>>> develop
          <Table.Column
            title='소개'
            dataIndex='introduction'
            key='introduction'
<<<<<<< HEAD
            width={300}
          />
          <Table.Column
            title='관심사'
            dataIndex='interests'
            key='interests'
            render={(interests) => {
              return interests.map((interset: string) => (
                <div
                  style={{
                    padding: '1px 1.5px',
                    borderRadius: '4px',
                    backgroundColor: '#f0f0f0',
                    display: 'inline-block',
                    margin: '2px',
                    color: '#333',
                    fontSize: '12px',
                    lineHeight: '1.5',
                    whiteSpace: 'nowrap',
                    cursor: 'default',
                  }}
                >
                  {interset}
                </div>
              ));
            }}
          />
          <Table.Column
            title='가입일'
            dataIndex='joinDate'
            key='joinDate'
            render={(date: string) => {
              return new Date(date).toLocaleDateString();
            }}
          />
          <Table.Column
            title='실행'
            key='action'
            render={(_, record: { userId: string }) => (
              <Space
                style={{
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                }}
              >
                <a
                  onClick={() => {
                    deleteUser({ userId: Number(record.userId) });
                  }}
                >
                  삭제
                </a>
              </Space>
            )}
          />
=======
          />
          <Table.Column title='관심사' dataIndex='interests' key='interests' />
          <Table.Column title='가입일' dataIndex='joinDate' key='joinDate' />
          <Table.Column title='실행' key='action' />
>>>>>>> develop
        </Table>
      </Card>
    </Container>
  );
}
