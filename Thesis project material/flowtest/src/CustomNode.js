/*import { useCallback } from 'react';
import { Handle, Position , OnConnectStart } from 'reactflow';


function CustomNode({ data }) {
  const onChange = useCallback((evt) => {
    console.log(evt.target.value);
  }, []);



  const handleClick = () => {
    setIsClicked(true);
    LaunchSPARQLquery()  //here the compatible ports are queried
  }

  return (
    <>
    {data.model.map( (p) => {
      return <>
      <Handle 
      style={{backgroundColor: p.isClicked ? '#fff' : '#000'}}
      position={PositionValue}
      id={p.id}
      />
      <p>{p.label}</p>
      </>
      })}
    </>
  );
}

export default CustomNode;*/