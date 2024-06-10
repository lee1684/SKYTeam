import styled from 'styled-components';
<<<<<<< HEAD
import { Card, Space, Table } from 'antd';
import { MoimApi } from '../../apis/moim';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
=======
import { Card, Table } from 'antd';
>>>>>>> develop

const Container = styled.div`
  padding: 32px;
`;

<<<<<<< HEAD
export default function MoimList() {
  const queryClient = useQueryClient();

  const { data } = useQuery({
    queryKey: ['moims'],
    queryFn: MoimApi.getMoimList,
  });

  const { mutate: deleteMoim } = useMutation({
    mutationFn: MoimApi.deleteMoim,
    onSuccess: () => {
      void queryClient.invalidateQueries({
        queryKey: ['moims'],
      });
    },
  });

  return (
    <Container>
      <Card title='모임 전체 목록' style={{ width: '100%' }}>
        <Table dataSource={data?.content}>
          <Table.Column title='ID' dataIndex='moimId' key='moimId' />
          <Table.Column
            title='제목'
            dataIndex='meetingTitle'
            key='meetingTitle'
            width={800}
          />

          <Table.Column
            title='카테고리'
            dataIndex='categoryName'
            key='categoryName'
            width={100}
          />
          <Table.Column
            title='실행'
            key='action'
            width={100}
            render={(_, record: { moimId: string }) => (
              <Space>
                <a
                  onClick={() => {
                    deleteMoim({ moimId: Number(record.moimId) });
                  }}
                >
                  삭제
                </a>
              </Space>
            )}
          />
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

export default function MoimList() {
  return (
    <Container>
      <Card title='모임 전체 목록' style={{ width: '100%' }}>
        <Table dataSource={data}>
          <Table.Column title='제목' dataIndex='nickname' key='nickname' />
          <Table.Column
            title='프로필 사진'
            dataIndex='profilePictureUrl'
            key='profilePictureUrl'
          />
          <Table.Column title='설명' dataIndex='gender' key='gender' />
          <Table.Column title='증표' dataIndex='address' key='address' />
          <Table.Column title='참가장소' dataIndex='address' key='address' />
          <Table.Column title='생성자' dataIndex='role' key='role' />
          <Table.Column
            title='참가비'
            dataIndex='introduction'
            key='introduction'
          />
          <Table.Column
            title='수용인원'
            dataIndex='interests'
            key='interests'
          />
          <Table.Column title='참가자' dataIndex='joinDate' key='joinDate' />
          <Table.Column title='미팅날짜' dataIndex='joinDate' key='joinDate' />
          <Table.Column title='공개여부' dataIndex='joinDate' key='joinDate' />
          <Table.Column title='실행' key='action' />
>>>>>>> develop
        </Table>
      </Card>
    </Container>
  );
}
