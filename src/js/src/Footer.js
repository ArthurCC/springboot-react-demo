import { Avatar, Button } from "antd";
import { Container } from "./Container";

export const Footer = ({ studentsNumber, isFetching, showModal }) => (
    <div>
        <Container>
            {studentsNumber ? (
                <Avatar
                    size="large"
                    style={{
                        backgroundColor: "#f56a00",
                        marginRight: "5px",
                    }}
                >
                    {studentsNumber}
                </Avatar>
            ) : null}

            {!isFetching ? (
                <Button type="primary" onClick={showModal}>
                    Add new student
                </Button>
            ) : null}
        </Container>
    </div>
);
