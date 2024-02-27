export const generateKeys = <T extends unknown[]>(key: string, ...props: T) => {
	return [key, ...props];
};
