from sympy import symbols, Eq, solve, sympify
from sympy.parsing.sympy_parser import parse_expr

def find_polynomial_roots(poly_str, variable='x'):
    """
    Finds the roots of a polynomial given as a string.

    Args:
        poly_str (str): Polynomial equation as string (e.g., "x^2 - 4x + 4")
        variable (str): The variable in the polynomial (default: 'x')

    Returns:
        list: Roots of the polynomial
    """
    try:
        x = symbols(variable)

        # Parse the polynomial string and convert to standard form (expr = 0)
        expr = parse_expr(poly_str.replace('^', '**'))
        equation = Eq(expr, 0)

        # Solve the equation
        roots = solve(equation, x)

        # Convert to numerical values if complex roots exist
        roots = [root.evalf() if hasattr(root, 'evalf') else root for root in roots]

        return roots

    except Exception as e:
        raise ValueError(f"Error processing polynomial: {e}")

# Example usage
if __name__ == "__main__":
    test_cases = [
        "x^2 - 4",                # Roots: [-2, 2]
        "x^2 + 2*x + 1",          # Root: -1 (double)
        "x^3 - 6*x^2 + 11*x - 6", # Roots: [1, 2, 3]
        "x^2 + 1",                # Roots: [-I, I] (imaginary)
        "3*x - 6.5"                 # Root: 2
    ]

    for poly in test_cases:
        print(f"Polynomial: {poly}")
        print(f"Roots: {find_polynomial_roots(poly)}")
        print()