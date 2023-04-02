import { Avatar, Button, Empty, Modal, notification, Spin, Table } from "antd";
import { useEffect, useState } from "react";
import "./App.css";
import { getAllStudents, getStudentCourses } from "./client";
import { Container } from "./Container";
import { Footer } from "./Footer";
import { AddStudentForm } from "./forms/AddStudentForm";

/**
 * App component
 * @returns
 */
export const App = () => {
    const antdColumns = [
        {
            title: "",
            key: "avatar",
            render: (_, student) => (
                <Avatar
                    size="large"
                    style={{
                        backgroundColor: "#f56a00",
                        verticalAlign: "middle",
                    }}
                >
                    {`${student.firstName.charAt(0)}${student.lastName.charAt(
                        0
                    )}`}
                </Avatar>
            ),
        },
        {
            title: "ID",
            dataIndex: "id",
            key: "id",
        },
        {
            title: "First name",
            dataIndex: "firstName",
            key: "firstName",
        },
        {
            title: "Last name",
            dataIndex: "lastName",
            key: "lastName",
        },
        {
            title: "Email",
            dataIndex: "email",
            key: "email",
        },
        {
            title: "Gender",
            dataIndex: "gender",
            key: "gender",
        },
        {
            title: "",
            key: "courses",
            render: (_, student) => (
                <Button
                    key={"courses_" + student.id}
                    onClick={() => onShowCoursesClick(student)}
                >
                    Courses
                </Button>
            ),
        },
    ];

    const [state, setState] = useState({
        students: [],
        isFetching: true,
        isAddStudentModalOpen: false,
        studentDetail: null,
        isCourseModalOpen: false,
    });
    // const [isFetching, setIsFetching] = useState(false);

    // for alert messages
    // const [messageApi, contextHolder] = message.useMessage();
    const [api, contextHolder] = notification.useNotification();

    // get all students on component load
    useEffect(() => {
        (async () => {
            try {
                const response = await getAllStudents();
                setState({
                    students: response.data.students,
                    isFetching: false,
                    isAddStudentModalOpen: false,
                    studentDetail: null,
                    isCourseModalOpen: false,
                });
            } catch (error) {
                console.error(
                    "Error while fetching students from server",
                    error
                );
                setState({
                    students: [],
                    isFetching: false,
                    isAddStudentModalOpen: false,
                    studentDetail: null,
                    isCourseModalOpen: false,
                });

                api["error"]({
                    message: error.message,
                    description: error.response.data.errorMessage,
                });
            }
        })();
    }, [api]);

    // Add student modal functions
    const showModal = () => setState({ ...state, isAddStudentModalOpen: true });

    const handleCancel = () =>
        setState({ ...state, isAddStudentModalOpen: false });

    const onFormSuccess = (student) => {
        setState({
            ...state,
            students: [...state.students, student],
            isAddStudentModalOpen: false,
        });

        api["success"]({
            message: "Employee added successfully",
        });
    };

    const onFormFailure = (errorMessage, errorDescription) =>
        api["error"]({
            message: errorMessage,
            description: errorDescription,
        });

    const onShowCoursesClick = async (student) => {
        try {
            const response = await getStudentCourses(student.id);
            console.log(response);
            setState({
                ...state,
                studentDetail: response.data.student,
                isCourseModalOpen: true,
            });
        } catch (error) {
            console.error(
                "Error while fetching student courses from server",
                error
            );
            api["error"]({
                message: error.message,
                description: error.response.data.errorMessage,
            });
        }
    };

    return (
        <Container>
            {/* Necessary for alerts display */}
            {contextHolder}

            <h1>Hello world Spring boot & react</h1>

            {state.isFetching ? (
                <Spin />
            ) : state.students && state.students.length ? (
                <Table
                    dataSource={state.students}
                    columns={antdColumns}
                    rowKey="id"
                    pagination={false}
                />
            ) : (
                <Empty description={<span>No students found</span>} />
            )}
            <Footer
                studentsNumber={state.students.length}
                isFetching={state.isFetching}
                showModal={showModal}
            />

            <Modal
                title="Add new student"
                open={state.isAddStudentModalOpen}
                onCancel={handleCancel}
                footer={[
                    <Button key="back" onClick={handleCancel}>
                        Cancel
                    </Button>,
                ]}
            >
                <h1>Add student form</h1>
                <AddStudentForm
                    onFormSuccess={onFormSuccess}
                    onFormFailure={onFormFailure}
                />
            </Modal>

            {/* Student course modal */}
            {state.studentDetail ? (
                <Modal
                    title="Student course modal"
                    open={state.isCourseModalOpen}
                    onCancel={() =>
                        setState({ ...state, isCourseModalOpen: false })
                    }
                    footer={[]}
                >
                    {/* TODO : improve this */}
                    <h1>
                        {state.studentDetail.firstName}{" "}
                        {state.studentDetail.lastName}
                    </h1>

                    <p>{state.studentDetail.email}</p>
                    <p>{state.studentDetail.gender}</p>

                    {state.studentDetail.courses &&
                    state.studentDetail.courses.length ? (
                        state.studentDetail.courses.map((course) => (
                            <div key={course.id}>
                                <p>{course.id}</p>
                                <p>{course.name}</p>
                                <p>{course.description}</p>
                                <p>{course.department}</p>
                                <p>{course.teacherName}</p>
                                <p>{course.startDate}</p>
                                <p>{course.endDate}</p>
                                <p>{course.grade}</p>
                            </div>
                        ))
                    ) : (
                        <h1>No courses</h1>
                    )}
                </Modal>
            ) : null}
        </Container>
    );
};
