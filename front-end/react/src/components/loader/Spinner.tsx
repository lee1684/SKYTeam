import styled from 'styled-components';

const SpinnerContainer = styled.div`
  z-index: 9999;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

const SpinnerPath = styled.path`
  fill: #a7afbf;
  transform-origin: center;

  animation: spinner 0.75s infinite linear;

  @keyframes spinner {
    100% {
      transform: rotate(360deg);
    }
  }
`;

export default function Spinner() {
  return (
    <SpinnerContainer>
      <svg
        width='64'
        height='64'
        viewBox='0 0 24 24'
        xmlns='http://www.w3.org/2000/svg'
      >
        <path
          d='M12,1A11,11,0,1,0,23,12,11,11,0,0,0,12,1Zm0,19a8,8,0,1,1,8-8A8,8,0,0,1,12,20Z'
          fill='#E1E6F1'
        />
        <SpinnerPath d='M12,4a8,8,0,0,1,7.89,6.7A1.53,1.53,0,0,0,21.38,12h0a1.5,1.5,0,0,0,1.48-1.75,11,11,0,0,0-21.72,0A1.5,1.5,0,0,0,2.62,12h0a1.53,1.53,0,0,0,1.49-1.3A8,8,0,0,1,12,4Z' />
      </svg>
    </SpinnerContainer>
  );
}
