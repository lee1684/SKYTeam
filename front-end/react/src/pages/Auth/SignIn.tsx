import { Button, Watermark } from 'antd';
import styled from 'styled-components';
import { colors } from '../../assets/colors';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 320px;
  margin: 0 auto;
  padding: 1rem;
  height: 100vh;
  box-sizing: border-box;
  justify-content: center;
`;

const Wrapper = styled.form`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  box-sizing: border-box;
  z-index: 10;
  background-color: ${colors.white};
  padding: 1rem;
`;

export default function SignIn() {
  return (
    <Watermark content={['SSALON']}>
      <Container>
        <Wrapper>
          <Button
            onClick={() => {
              window.location.replace(
                'https://ssalon.co.kr/oauth2/authorization/kakao'
              );
            }}
            type='primary'
          >
            카카오로 로그인
          </Button>
        </Wrapper>
      </Container>
    </Watermark>
  );
}
