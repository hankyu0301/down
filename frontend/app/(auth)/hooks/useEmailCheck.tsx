import { useState, useCallback } from "react";
import { useFormContext } from "react-hook-form";

import { postEmailCheck } from "@/api/signup";
import {
  Input,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from "@/components/ui";

type EmailCheckStatus = {
  success: boolean;
  data: {checkedEmail: string, available: boolean};
  message: string;
};

export const useEmailCheck = () => {
  const [emailCheckStatus, setEmailCheckStatus] = useState<EmailCheckStatus | null>(null);
  const { control, trigger, getValues, formState: { errors } } = useFormContext();

  const onCheckValidEmail = useCallback(async () => {
    const input = await trigger("email");
    if (!input) return;

    const email = getValues("email");
    const result = await postEmailCheck(email);
    setEmailCheckStatus(result);
  }, [getValues, trigger])

  const EmailCheckField = () => (
    <FormField
      control={control}
      name="email"
      render={({ field }) => (
        <FormItem>
          <FormLabel>이메일</FormLabel>
          <FormControl>
            <Input
              placeholder="email@example.com"
              {...field}
              onBlur={onCheckValidEmail}
              />
            </FormControl>
            <FormMessage />
            {!errors.email && emailCheckStatus && (
              <p className="text-sm font-medium text-stone-500">
                {emailCheckStatus.message}
              </p>
            )}
            {!errors.email && emailCheckStatus?.success && (
              <p className="text-sm font-medium text-stone-500">
                {emailCheckStatus.message}
              </p>
            )}
        </FormItem>
      )}
    />
  );

  return { EmailCheckField, emailCheckStatus };
};
