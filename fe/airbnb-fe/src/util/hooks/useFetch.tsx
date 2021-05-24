import React, { useState, useEffect } from 'react';

type FetchProps = {
  url: string,
  options: object
};

function useFetch({ url, options }: FetchProps): { response: object|null, isLoading: boolean|null, error: Error|null } {
  const [response, setResponse] = useState<object|null>(null);
  const [isLoading, setIsLoading] = useState<boolean|null>(null);
  const [error, setError] = useState<Error|null>(null);

  useEffect(() => {

  }, []);

  return { response, isLoading, error };
}

export default useFetch;
