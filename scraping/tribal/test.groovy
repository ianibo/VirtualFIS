#!/home/ibbo/.gvm/groovy/current/bin/groovy

def str="/wEPDwULLTE3NDkyMTY0NjMPZBYCZg9kFgICAw9kFgwCBQ9kFgICBQ8WAh4HVmlzaWJsZWhkAgcPDxYCHwBoZGQCCQ8WAh8AaGQCCw9kFgQCAQ8WAh8AZxYKZg8WAh4JaW5uZXJodG1sBQ1Zb3VyIFJlc3VsdHM6ZAIBDw8WBB4EVGV4dAVMU2NyaXB0IG5vdCBlbmFibGVkLiBBc2NlbmRpbmcvRGVzY2VuZGluZyBPcHRpb24gaXMgbm90IHVzZWQgZm9yIERlZmF1bHQgU29ydB8AaGRkAgMPZBYEAgEPZBYCAgEPZBYCZg8PFgYeCFBhZ2VTaXplAgUeCVRvdGFsUm93cwIlHgpQYWdlTnVtYmVyAgFkFgJmDw8WBh4IQ3NzQ2xhc3MFH3BhZ2luZ19wcmV2aW91c19idXR0b25fZGlzYWJsZWQeB0VuYWJsZWRoHgRfIVNCAgJkZAIDDzwrAA0DAA8WBh8DAgUeC18hRGF0YUJvdW5kZx4LXyFJdGVtQ291bnQCBWQBDxQrAAYUKwAFFgQeCURhdGFGaWVsZAUQUHJvdmlkZXJDaGVja2JveB4KSGVhZGVyVGV4dAUUQWRkIFByb3ZpZGVyIHRvIExpc3QWBB8GBRByZXN1bHRzX2NvbHVtbl8xHwgCAmRkZBQrAAUWBB8LBQxQcm92aWRlclR5cGUfDAUQVHlwZSBvZiBQcm92aWRlchYEHwYFEHJlc3VsdHNfY29sdW1uXzIfCAICZGRkFCsABRYEHwsFDU5hbWVBbmRPdGhlcnMfDAUETmFtZRYEHwYFEHJlc3VsdHNfY29sdW1uXzMfCAICZGRkFCsABRYEHwsFCVRlbGVwaG9uZR8MBQlUZWxlcGhvbmUWBB8GBRByZXN1bHRzX2NvbHVtbl80HwgCAmRkZBQrAAUWBB8LBQRXYXJkHwwFBFdhcmQWBB8GBRByZXN1bHRzX2NvbHVtbl81HwgCAmRkZBQrAAUWBB8LBRREaXN0YW5jZUZyb21TZWFyY2hlch8MBRFEaXN0YW5jZSBGcm9tIFlvdRYEHwYFEHJlc3VsdHNfY29sdW1uXzYfCAICZGRkDxQrAQYCBwIIAgkCCAIIAggWAwWIAVRyaWJhbC5FWU8uUHVibGljRW5xdWlyeS5FWU9Qcm92aWRlckNoZWNrYm94Qm91bmRGaWVsZCwgVHJpYmFsLkVZTy5QdWJsaWNFbnF1aXJ5LCBWZXJzaW9uPTEyLjQuOC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPW51bGwFggFUcmliYWwuRVlPLlB1YmxpY0VucXVpcnkuRVlPQ2hlY2tFbXB0eUJvdW5kRmllbGQsIFRyaWJhbC5FWU8uUHVibGljRW5xdWlyeSwgVmVyc2lvbj0xMi40LjguMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1udWxsBYIBVHJpYmFsLkVZTy5QdWJsaWNFbnF1aXJ5LkVZT1Byb3ZpZGVyTmFtZVdpdGhMaW5rLCBUcmliYWwuRVlPLlB1YmxpY0VucXVpcnksIFZlcnNpb249MTIuNC44LjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49bnVsbAkWAh4MUGFnZXJWaXNpYmxlaBYCZg9kFgwCAQ9kFgxmDw8WAh8CBSJBYmFjdXMgRWFybHkgTGVhcm5pbmcgTnVyc2VyeXw0OTZ8ZGQCAQ8PFgIfAgULRGF5IE51cnNlcnlkZAICDw8WAh8CBSJBYmFjdXMgRWFybHkgTGVhcm5pbmcgTnVyc2VyeXw0OTZ8ZGQCAw8PFgIfAgUNMDIwIDg2NzcgOTExN2RkAgQPDxYCHwIFDFN0IExlb25hcmQnc2RkAgUPDxYCHwIFFjxlbT5Ob3QgQXZhaWxhYmxlPC9lbT5kZAICD2QWDGYPDxYCHwIFEUFkYW1zLCBBbWluYXwzNjl8ZGQCAQ8PFgIfAgULQ2hpbGRtaW5kZXJkZAICDw8WAh8CBRFBZGFtcywgQW1pbmF8MzY5fGRkAgMPDxYCHwIFDTAyMCA4NzY5IDI1MzhkZAIEDw8WAh8CBQxTdCBMZW9uYXJkJ3NkZAIFDw8WAh8CBRY8ZW0+Tm90IEF2YWlsYWJsZTwvZW0+ZGQCAw9kFgxmDw8WAh8CBRRBbXBhdywgVmVyb25pY2F8NTcxfGRkAgEPDxYCHwIFC0NoaWxkbWluZGVyZGQCAg8PFgIfAgUUQW1wYXcsIFZlcm9uaWNhfDU3MXxkZAIDDw8WAh8CBQ0wMjAgNzQ5OCA3NjA5ZGQCBA8PFgIfAgUJU3RvY2t3ZWxsZGQCBQ8PFgIfAgUWPGVtPk5vdCBBdmFpbGFibGU8L2VtPmRkAgQPZBYMZg8PFgIfAgUdQWxpZWZlaC1DYW1wYmVsbCwgQWduZXN8NTI5NnxkZAIBDw8WAh8CBQtDaGlsZG1pbmRlcmRkAgIPDxYCHwIFHUFsaWVmZWgtQ2FtcGJlbGwsIEFnbmVzfDUyOTZ8ZGQCAw8PFgIfAgUNMDc5NTYgMjI1IDkyOWRkAgQPDxYCHwIFD1N0cmVhdGhhbSBTb3V0aGRkAgUPDxYCHwIFFjxlbT5Ob3QgQXZhaWxhYmxlPC9lbT5kZAIFD2QWDGYPDxYCHwIFEkFsbGVuLCBTaW1vbmV8OTExfGRkAgEPDxYCHwIFC0NoaWxkbWluZGVyZGQCAg8PFgIfAgUSQWxsZW4sIFNpbW9uZXw5MTF8ZGQCAw8PFgIfAgUNMDc4ODAgOTExIDExN2RkAgQPDxYCHwIFClR1bHNlIEhpbGxkZAIFDw8WAh8CBRY8ZW0+Tm90IEF2YWlsYWJsZTwvZW0+ZGQCBg8PFgIfAGhkZAIFD2QWAgIBD2QWAmYPDxYGHwMCBR8EAiUfBQIBZBYCZg8PFgYfBgUfcGFnaW5nX3ByZXZpb3VzX2J1dHRvbl9kaXNhYmxlZB8HaB8IAgJkZAIHDw8WAh8CBQpOZXcgU2VhcmNoZGQCAw9kFgICBg88KwANAGQCDQ8WAh8AaGQCDw8PFgIfAGhkZBgFBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WBQVYY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRHcmlkVmlldzEkY3RsMDIkcHJvdmlkZXJfY2JfQWJhY3VzIEVhcmx5IExlYXJuaW5nIE51cnNlcnl8NDk2fAVHY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRHcmlkVmlldzEkY3RsMDMkcHJvdmlkZXJfY2JfQWRhbXMsIEFtaW5hfDM2OXwFSmN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkR3JpZFZpZXcxJGN0bDA0JHByb3ZpZGVyX2NiX0FtcGF3LCBWZXJvbmljYXw1NzF8BVNjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJEdyaWRWaWV3MSRjdGwwNSRwcm92aWRlcl9jYl9BbGllZmVoLUNhbXBiZWxsLCBBZ25lc3w1Mjk2fAVIY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRHcmlkVmlldzEkY3RsMDYkcHJvdmlkZXJfY2JfQWxsZW4sIFNpbW9uZXw5MTF8BTVjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJHNob3BwaW5nQmFza2V0UHJvdmlkZXJzR3JpZA9nZAUjY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMSRHcmlkVmlldzEPPCsACgEIAgFkBSFjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJFdpemFyZDEPEGQUKwABZmZkBTFjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIxJFdpemFyZDEkV2l6YXJkTXVsdGlWaWV3Dw9kZmSjWv7TfLSMLbT5hDBMBpj+Zfi8aA=="

println("Decoded: ${new String(str.decodeBase64())}");