"use client";
import {
	QueryClient,
	QueryClientConfig,
	QueryClientProvider,
} from "@tanstack/react-query";
import { PropsWithChildren, useState } from "react";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

interface ProviderProps extends PropsWithChildren {
	queryConfig?: QueryClientConfig;
}

export const QueryProvider = ({ children, queryConfig }: ProviderProps) => {
	const [queryClient] = useState(
		() =>
			new QueryClient({
				defaultOptions: {
					...queryConfig?.defaultOptions,
					queries: {
						...queryConfig?.defaultOptions?.queries,
					},
				},
			})
	);

	return (
		<QueryClientProvider client={queryClient}>
			{children}
			<ReactQueryDevtools initialIsOpen={false} />
		</QueryClientProvider>
	);
};
