"use client";
import { FieldValues, SubmitHandler, UseFormReturn } from "react-hook-form";
import { HTMLAttributes } from "react";

import { Form } from "@/components/ui";

type FormFieldWrapperProps = {
  method: UseFormReturn<FieldValues>;
  onSubmit: SubmitHandler<FieldValues>;
} & HTMLAttributes<HTMLDivElement>;

const FormFieldWrapper = ({ children, method, onSubmit }: FormFieldWrapperProps) => {
  return (
    <Form {...method}>
      <form className="space-y-8" onSubmit={method.handleSubmit(onSubmit)}>
        {children}
      </form>
    </Form>
  );
};

export default FormFieldWrapper;
