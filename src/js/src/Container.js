// props.children basically render what is define inside the component
export const Container = (props) => (
    <div style={{ width: "1400px", margin: "0 auto", textAlign: "center" }}>
        {props.children}
    </div>
);
