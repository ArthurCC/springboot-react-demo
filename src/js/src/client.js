import axios from "axios";

/**
 * fetch all users
 * @returns
 */
export const getAllStudents = async () => {
    const res = await axios.get(
        `${process.env.REACT_APP_STUDENT_API_URL}/api/students`
    );
    return res.data;
};

export const postStudent = async (student) => {
    const res = await axios.post(
        `${process.env.REACT_APP_STUDENT_API_URL}/api/students`,
        student
    );
    return res.data;
};

export const getStudentCourses = async (studentId) => {
    const res = await axios.get(
        `${process.env.REACT_APP_STUDENT_API_URL}/api/students/${studentId}`
    );
    return res.data;
};

export const deleteStudent = async (studentId) => {
    const res = await axios.delete(
        `${process.env.REACT_APP_STUDENT_API_URL}/api/students/${studentId}`
    );
    return res.data;
};

export const updateStudent = async (studentId, student) => {
    const res = await axios.put(
        `${process.env.REACT_APP_STUDENT_API_URL}/api/students/${studentId}`,
        student
    );
    return res.data;
};
