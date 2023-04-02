import { Modal } from "antd";

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
    >
        {/* TODO : improve this */}
        <h1>
            {studentDetail.firstName} {studentDetail.lastName}
        </h1>

        <p>{studentDetail.email}</p>
        <p>{studentDetail.gender}</p>

        {studentDetail.courses && studentDetail.courses.length ? (
            studentDetail.courses.map((course) => (
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
);
