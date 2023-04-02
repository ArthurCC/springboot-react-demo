import { Modal, Table } from "antd";

const courseTableColumns = [
    {
        title: "Name",
        dataIndex: "name",
        key: "name",
    },
    {
        title: "Description",
        dataIndex: "description",
        key: "description",
    },
    {
        title: "Department",
        dataIndex: "department",
        key: "department",
    },
    {
        title: "Teacher",
        dataIndex: "teacherName",
        key: "teacherName",
    },
    {
        title: "Start",
        dataIndex: "startDate",
        key: "startDate",
    },
    {
        title: "End",
        dataIndex: "endDate",
        key: "endDate",
    },
    {
        title: "Grade",
        dataIndex: "grade",
        key: "grade",
    },
];

export const StudentDetailModal = ({
    studentDetail,
    isStudentDetailModalOpen,
    handleCancel,
}) => (
    <Modal
        title="Student course modal"
        open={isStudentDetailModalOpen}
        onCancel={handleCancel}
        footer={[]}
        width={1000}
    >
        <h1>
            {studentDetail.firstName} {studentDetail.lastName}
        </h1>

        <p>{studentDetail.email}</p>
        <p>{studentDetail.gender}</p>

        {studentDetail.courses && studentDetail.courses.length ? (
            <>
                <h2>Courses</h2>
                <Table
                    dataSource={studentDetail.courses}
                    columns={courseTableColumns}
                    rowKey="id"
                    pagination={false}
                />
            </>
        ) : (
            <h2>No courses</h2>
        )}
    </Modal>
);
