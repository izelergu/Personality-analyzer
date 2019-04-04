def read_dic(filepath):
    '''
    Reads a LIWC lexicon from a file in the .dic format, returning a tuple of
    (lexicon, category_names), where:
    * `lexicon` is a dict mapping string patterns to lists of category names
    * `categories` is a list of category names (as strings)

    '''
    # category_mapping is a mapping from integer string to category name
    category_mapping = {}
    # category_names is equivalent to category_mapping.values() but retains original ordering
    category_names = []
    lexicon = {}
    # the mode is incremented by each '%' line in the file
    mode = 0
    for line in open(filepath):
        tsv = line.strip()
        if tsv:
            parts = tsv.split()
            if parts[0] == '%':
                mode += 1
            elif mode == 1:
                # definining categories
                category_names.append(parts[1])
                category_mapping[parts[0]] = parts[1]
            elif mode == 2:
                lexicon[parts[0]] = [category_mapping[category_id] for category_id in parts[1:]]
    return lexicon, category_names


def _build_trie(lexicon):
    '''
    Build a character-trie from the plain pattern_string -> categories_list
    mapping provided by `lexicon`.

    Some LIWC patterns end with a `*` to indicate a wildcard match.
    '''
    trie = {}
    for pattern, category_names in lexicon.items():
        cursor = trie
        for i in range(0, len(pattern)):
            if pattern[i] == '*':
                cursor['*'] = category_names
                break
            elif pattern[i] == '(':
                tempcursor = _build_sub_trie(cursor, i+1, pattern, category_names)
                cursor = tempcursor
                break
            if pattern[i] not in cursor:
                cursor[pattern[i]] = {}
            cursor = cursor[pattern[i]]
        cursor['$'] = category_names
    return trie


def _build_sub_trie(cursor, index, pattern, category_names):
    '''
    Build the sub-trie from the pattern.
    Ex: accept(ed|s)
    :param cursor:
    :param index:
    :param pattern:
    :param category_names:
    :return:
    '''
    startcursor = cursor
    options = []
    option = ""
    lastindex = 0
    for i in range(index, len(pattern)):
        if pattern[i] == '|' and option != "":
            options.append(option)
            option = ""
        elif pattern[i] == ')' and option != "":
            options.append(option)
            lastindex = i + 1
            break
        else:
            option = option + pattern[i]

    for opt in options:
        for char in opt:
            if char not in cursor:
                cursor[char] = {}
            cursor = cursor[char]
        if lastindex < len(pattern):
            for i in range(lastindex, len(pattern)):
                if pattern[i] not in cursor:
                    cursor[pattern[i]] = {}
                cursor = cursor[pattern[i]]
        cursor['$'] = category_names
        cursor = startcursor
    return cursor


def _search_trie(trie, token, token_i=0):
    '''
    Search the given character-trie for paths that match the `token` string.
    '''
    if '*' in trie:
        return trie['*']
    elif '$' in trie and token_i == len(token):
        return trie['$']
    elif token_i < len(token):
        char = token[token_i]
        if char in trie:
            return _search_trie(trie[char], token, token_i + 1)
    return []


def load_token_parser(filepath):
    '''
    Reads a LIWC lexicon from a file in the .dic format, returning a tuple of
    (parse, category_names), where:
    * `parse` is a function from a token to a list of strings (potentially
      empty) of matching categories
    * `category_names` is a list of strings representing all LIWC categories in
      the lexicon
    '''
    lexicon, category_names = read_dic(filepath)
    trie = _build_trie(lexicon)
    def parse_token(token):
        for category_name in _search_trie(trie, token):
            yield category_name
    return parse_token, category_names
