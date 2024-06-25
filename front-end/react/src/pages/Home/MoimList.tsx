import styled from 'styled-components';
import { Card, Space, Table } from 'antd';
import { MoimApi } from '../../apis/moim';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

const Container = styled.div`
  padding: 32px;
`;

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
        </Table>
      </Card>
    </Container>
  );
}
