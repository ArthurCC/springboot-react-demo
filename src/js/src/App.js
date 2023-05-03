import { ExpandAltOutlined } from "@ant-design/icons";
import { Avatar, Button, Empty, Modal, Spin, Table, notification } from "antd";
import { useEffect, useState } from "react";
import "./App.css";
import { Container } from "./Container";
import { Footer } from "./Footer";
import { StudentDetailModal } from "./StudentDetailModal";
import { deleteStudent, getAllStudents, getStudentCourses } from "./client";
import { AddStudentForm } from "./forms/AddStudentForm";

/**
 * App component
 * @returns
 */
export const App = () => {
    const studentTableColumns = [
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
                    icon={<ExpandAltOutlined />}
                    key={`courses_${student.id}`}
                    onClick={() => onShowCoursesClick(student.id)}
                />
            ),
        },
        {
            title: "Actions",
            key: "actions",
            render: (_, student) => (
                <>
                    <Button
                        key={`update_${student.id}`}
                        onClick={() =>
                            setState({
                                ...state,
                                studentDetail: student,
                                isAddStudentModalOpen: true,
                            })
                        }
                    >
                        Update
                    </Button>
                    <Button
                        key={`delete_${student.id}`}
                        onClick={() =>
                            setState({
                                ...state,
                                studentDetail: student,
                                isDeleteStudentModalOpen: true,
                            })
                        }
                        danger
                    >
                        Delete
                    </Button>
                </>
            ),
        },
    ];

    const [state, setState] = useState({
        students: [],
        isFetching: true,
        isAddStudentModalOpen: false,
        studentDetail: null,
        isStudentDetailModalOpen: false,
        isDeleteStudentModalOpen: false,
    });

    // for alert messages
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
                    isStudentDetailModalOpen: false,
                    isDeleteStudentModalOpen: false,
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
                    isStudentDetailModalOpen: false,
                    isDeleteStudentModalOpen: false,
                });

                api["error"]({
                    message: error.message,
                    description: error.response?.data.errorMessage,
                });
            }
        })();
    }, [api]);

    // Add student modal functions
    const showModal = () =>
        setState({
            ...state,
            studentDetail: null,
            isAddStudentModalOpen: true,
        });

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

    const onFormSuccessUpdate = (student) => {
        setState({
            ...state,
            students: state.students.map((s) =>
                s.id === student.id ? student : s
            ),
            isAddStudentModalOpen: false,
        });

        api["success"]({
            message: "Employee updated successfully",
        });
    };

    const onFormFailure = (errorMessage, errorDescription) =>
        api["error"]({
            message: errorMessage,
            description: errorDescription,
        });

    const onShowCoursesClick = async (studentId) => {
        try {
            const response = await getStudentCourses(studentId);
            setState({
                ...state,
                studentDetail: response.data.student,
                isStudentDetailModalOpen: true,
            });
        } catch (error) {
            console.error(
                "Error while fetching student courses from server",
                error
            );
            api["error"]({
                message: error.message,
                description: error.response?.data.errorMessage,
            });
        }
    };

    const onConfirmDeleteStudentClick = async (studentId) => {
        try {
            await deleteStudent(studentId);
            setState({
                ...state,
                students: state.students.filter((s) => s.id !== studentId),
                isDeleteStudentModalOpen: false,
            });
            api["success"]({
                message: "Employee deleted",
            });
        } catch (error) {
            console.error("Error while deleting student", error);
            setState({ ...state, isDeleteStudentModalOpen: false });
            api["error"]({
                message: error.message,
                description: error.response?.data.errorMessage,
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
                    columns={studentTableColumns}
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
                    onFormSuccess={
                        state.studentDetail
                            ? onFormSuccessUpdate
                            : onFormSuccess
                    }
                    onFormFailure={onFormFailure}
                    studentDetail={state.studentDetail}
                />
            </Modal>

            {/* Student course modal */}
            {state.studentDetail ? (
                <StudentDetailModal
                    studentDetail={state.studentDetail}
                    isStudentDetailModalOpen={state.isStudentDetailModalOpen}
                    handleCancel={() =>
                        setState({ ...state, isStudentDetailModalOpen: false })
                    }
                />
            ) : null}

            {state.studentDetail ? (
                <Modal
                    title="Delete student"
                    open={state.isDeleteStudentModalOpen}
                    onOk={() =>
                        onConfirmDeleteStudentClick(state.studentDetail.id)
                    }
                    onCancel={() =>
                        setState({ ...state, isDeleteStudentModalOpen: false })
                    }
                >
                    Delete student {state.studentDetail.id} permanently ?
                </Modal>
            ) : null}
        </Container>
    );
};
