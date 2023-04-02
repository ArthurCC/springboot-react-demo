import axios from "axios";

/**
 * fetch all users
 * @returns
 */
export const getAllStudents = async () => {
    const res = await axios.get("/api/students");
    return res.data;
};

export const postStudent = async (student) => {
    const res = await axios.post("/api/students", student);
    return res.data;
};

export const getStudentCourses = async (studentId) => {
    const res = await axios.get(`/api/students/${studentId}`);
    return res.data;
};
