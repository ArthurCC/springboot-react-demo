import { Button, Input, Tag } from "antd";
import { Field, Formik } from "formik";
import { postStudent, updateStudent } from "../client";

export const AddStudentForm = ({
    onFormSuccess,
    onFormFailure,
    studentDetail,
}) => (
    <Formik
        enableReinitialize
        initialValues={
            studentDetail
                ? studentDetail
                : {
                      firstName: "",
                      lastName: "",
                      email: "",
                      gender: "MALE",
                  }
        }
        validate={(values) => {
            const errors = {};
            if (!values.firstName.trim()) {
                errors.firstName = "Required";
            }
            if (!values.lastName.trim()) {
                errors.lastName = "Required";
            }
            if (!values.email) {
                errors.email = "Required";
            } else if (
                !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(values.email)
            ) {
                errors.email = "Invalid email address";
            }
            return errors;
        }}
        onSubmit={(values, { resetForm, setSubmitting }) => {
            (async () => {
                const studentInput = {
                    firstName: values.firstName.trim(),
                    lastName: values.lastName.trim(),
                    email: values.email,
                    gender: values.gender,
                };

                // API call
                try {
                    const response = studentDetail
                        ? await updateStudent(studentDetail.id, studentInput)
                        : await postStudent(studentInput);

                    onFormSuccess(response.data.student);
                    resetForm();
                } catch (error) {
                    console.error(
                        "Error while submitting student to server",
                        error
                    );

                    onFormFailure(
                        error.message,
                        error.response?.data.errorMessage
                    );
                    // Keep submit button active if post failed
                    setSubmitting(false);
                }
            })();
        }}
    >
        {({
            values,
            errors,
            touched,
            handleChange,
            handleBlur,
            handleSubmit,
            isSubmitting,
            // this is necessary if we don't use the default html button
            submitForm,
            isValid,
            dirty,
            /* and other goodies */
        }) => (
            <form onSubmit={handleSubmit}>
                {errors.firstName && touched.firstName && (
                    <Tag style={{ backgroundColor: "#f50" }}>
                        {errors.firstName}
                    </Tag>
                )}
                <Input
                    style={{ marginBottom: "5px" }}
                    name="firstName"
                    onChange={handleChange}
                    onBlur={handleBlur}
                    value={values.firstName}
                    placeholder="first name"
                />
                {errors.lastName && touched.lastName && (
                    <Tag style={{ backgroundColor: "#f50" }}>
                        {errors.lastName}
                    </Tag>
                )}
                <Input
                    style={{ marginBottom: "5px" }}
                    name="lastName"
                    onChange={handleChange}
                    onBlur={handleBlur}
                    value={values.lastName}
                    placeholder="last name"
                />
                {errors.email && touched.email && (
                    <Tag style={{ backgroundColor: "#f50" }}>
                        {errors.email}
                    </Tag>
                )}
                <Input
                    style={{ marginBottom: "5px" }}
                    name="email"
                    type="email"
                    onChange={handleChange}
                    onBlur={handleBlur}
                    value={values.email}
                    placeholder="email"
                    disabled={studentDetail}
                />
                <br />
                <div role="group" aria-labelledby="my-radio-group">
                    <label>
                        <Field type="radio" name="gender" value="MALE" />
                        <span style={{ padding: "5px" }}>Male</span>
                    </label>
                    <label>
                        <Field type="radio" name="gender" value="FEMALE" />
                        <span style={{ padding: "5px" }}>Female</span>
                    </label>
                </div>
                {errors.gender && touched.gender && errors.gender}
                <Button
                    type="submit"
                    disabled={isSubmitting || !isValid || !dirty}
                    onClick={submitForm}
                >
                    Submit
                </Button>
            </form>
        )}
    </Formik>
);
