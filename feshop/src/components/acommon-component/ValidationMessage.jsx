const ValidationMessage = ({ errorResponse, field }) => {
    return (
      <>
        {!!errorResponse[field] ? (
          <p style={{color:"red"}} className="validation-message">{errorResponse[field]}</p>
        ) : (
          ""
        )}
      </>
    );
  };
  export default ValidationMessage;