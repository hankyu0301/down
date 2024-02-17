import create from "zustand";

const initialState = {
    email: "",
    emailCode: "",
    password: "",
    nickname: "",
    termsOfService: false,
    privacyPolicy: false,
    marketingConsent: false,
};

type SignUpState = typeof initialState;

export const useSignUpStore = create<SignUpState>((set) => ({
    ...initialState,
    setEmail: (email: string) => set({ email }),
    setEmailCode: (emailCode: string) => set({ emailCode }),
    setPassword: (password: string) => set({ password }),
    setNickname: (nickname: string) => set({ nickname }),
    setTermsOfService: (termsOfService: boolean) => set({ termsOfService }),
    setPrivacyPolicy: (privacyPolicy: boolean) => set({ privacyPolicy }),
    setMarketingConsent: (marketingConsent: boolean) => set({ marketingConsent }),
    reset: () => set(initialState),
}));