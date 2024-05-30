import styled from 'styled-components';
import { Card, Table } from 'antd';

const Container = styled.div`
  padding: 32px;
`;

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
  return (
    <Container>
      <Card title='유저 전체 목록' style={{ width: '100%' }}>
        <Table dataSource={data}>
          <Table.Column title='닉네임' dataIndex='nickname' key='nickname' />
          <Table.Column
            title='프로필 사진'
            dataIndex='profilePictureUrl'
            key='profilePictureUrl'
          />
          <Table.Column title='성별' dataIndex='gender' key='gender' />
          <Table.Column title='주소' dataIndex='address' key='address' />
          <Table.Column title='역할' dataIndex='role' key='role' />
          <Table.Column
            title='소개'
            dataIndex='introduction'
            key='introduction'
          />
          <Table.Column title='관심사' dataIndex='interests' key='interests' />
          <Table.Column title='가입일' dataIndex='joinDate' key='joinDate' />
          <Table.Column title='실행' key='action' />
        </Table>
      </Card>
    </Container>
  );
}
